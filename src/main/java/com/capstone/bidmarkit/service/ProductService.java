package com.capstone.bidmarkit.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Like;
import co.elastic.clients.elasticsearch._types.query_dsl.MoreLikeThisQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.capstone.bidmarkit.domain.*;
import com.capstone.bidmarkit.dto.*;
import com.capstone.bidmarkit.repository.*;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;
    private final BidRepository bidRepository;
    private final Storage storage;
    private final ElasticsearchClient client;
    private final HistoryService historyService;
    private final ChatRoomRepository chatRoomRepository;
    private final ProductUpsertScheduleRepository productUpsertScheduleRepository;
    private final RedisTemplate redisTemplate;
    private final RedissonClient redissonClient;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @Value("${redis.schedule.key}")
    private String scheduleKey;

    @Value("${redis.product-bid.key}")
    private String productBidKey;

    @Value("${redis.product-bid.wait-time}")
    private int waitTime;

    @Value("${redis.product-bid.lease-time}")
    private int leaseTime;

    @Value("${redis.product-upsert.size}")
    private int upsertScheduleSize;

    @Value("${redis.product-upsert.schedule-id}")
    private String scheduleId;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public AddProductResponse save(String memberId, AddProductRequest dto) throws IOException {
        if(dto.getInitPrice() > dto.getPrice())
            throw new IllegalArgumentException("Init price should be higher than Instant purchase price");

        Product newProduct = productRepository.save(
                Product.builder()
                        .memberId(memberId)
                        .name(dto.getProductName())
                        .category(dto.getCategory())
                        .content(dto.getContent())
                        .initPrice(dto.getInitPrice())
                        .price(dto.getPrice())
                        .deadline(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusDays(dto.getDeadline()))
                        .build()
        );

        List<ProductImg> images = new ArrayList<>();
        for (int i = 0; i < dto.getImages().size(); i++) {
            MultipartFile image = dto.getImages().get(i);
            String uuid = UUID.randomUUID().toString();
            storage.create(
                    BlobInfo.newBuilder(bucketName, uuid)
                            .setContentType(image.getContentType())
                            .build(),
                    image.getInputStream()
            );

            ProductImg newImage = productImgRepository.save(
                    ProductImg.builder()
                            .product(newProduct)
                            .imgUrl("https://storage.googleapis.com/" + bucketName + '/' + uuid)
                            .isThumbnail(i == 0)
                            .build()
            );
            images.add(newImage);
        }

        newProduct.setImages(images);

        upsertProductsToElastic(new ElasticProduct(newProduct));

        return new AddProductResponse(newProduct.getId());
    }

    public Page<ProductBriefResponse> findAllOrderByDeadlineAsc(Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QProduct product = QProduct.product;
        QProductImg productImg = QProductImg.productImg;

        List<ProductBriefResponse> results = queryFactory
                .select(
                        Projections.constructor(
                        ProductBriefResponse.class,
                        productImg.imgUrl, product.name, product.category, product.id,
                        product.bidPrice, product.price, product.state, product.deadline)
                )
                .from(product)
                .leftJoin(product.images, productImg)
                .groupBy(product.id, productImg.imgUrl)
                .where(productImg.isThumbnail.isTrue())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(results, pageable, queryFactory.select(product.count()).from(product)::fetchCount);
    }

    public Page<GetPurchaseResponse> findAllPurchased(String memberId, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QProduct product = QProduct.product;
        QProductImg productImg = QProductImg.productImg;
        QTrade trade = QTrade.trade;

        List<GetPurchaseResponse> results = queryFactory
                .select(
                        Projections.constructor(
                                GetPurchaseResponse.class,
                                productImg.imgUrl, product.name, product.id, trade.price.avg().intValue())
                )
                .from(trade)
                .where(trade.buyerId.eq(memberId))
                .leftJoin(trade.product, product)
                .leftJoin(product.images, productImg)
                .groupBy(product.id, productImg.imgUrl)
                .where(productImg.isThumbnail.isTrue())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(results, pageable, queryFactory.select(trade.count()).from(trade).where(trade.buyerId.eq(memberId))::fetchCount);
    }

    public Page<ProductBriefResponse> findAllSale(String memberId, int state, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QProduct product = QProduct.product;
        QProductImg productImg = QProductImg.productImg;

        BooleanBuilder whereClause = new BooleanBuilder(product.memberId.eq(memberId));
        if(0 <= state && state < 4) whereClause.and(product.state.eq(state));

        List<ProductBriefResponse> results = queryFactory
                .select(
                        Projections.constructor(
                                ProductBriefResponse.class,
                                productImg.imgUrl, product.name, product.category, product.id,
                                product.bidPrice, product.price, product.state, product.deadline)
                )
                .from(product)
                .where(whereClause)
                .leftJoin(product.images, productImg)
                .groupBy(product.id, productImg.imgUrl)
                .where(productImg.isThumbnail.isTrue())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(results, pageable, queryFactory.select(product.count()).from(product).where(whereClause)::fetchCount);
    }

    public ProductDetailResponse findDetail(String memberId, int productId) {
        ProductDetailResponse res = new ProductDetailResponse();

        Product findProduct = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if(!memberId.isEmpty()) historyService.upsertSearchHistory(memberId, findProduct.getName(), findProduct.getCategory());

        res.setImages(productImgRepository.findByProductId(productId)
                .stream()
                .map(ProductImg::getImgUrl)
                .collect(Collectors.toList()));
        res.setProductName(findProduct.getName());
        res.setCategory(findProduct.getCategory());
        res.setBidPrice(findProduct.getBidPrice());
        res.setInitPrice(findProduct.getInitPrice());
        res.setPrice(findProduct.getPrice());
        res.setState(findProduct.getState());
        res.setDeadline(findProduct.getDeadline());
        res.setSellerName(findProduct.getMemberId());
        res.setContent(findProduct.getContent());

        return res;
    }

    @Transactional
    public void purchaseProduct(String memberId, int productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // 상품 상태가 판매 중이 아닐 경우, 예외 발생
        if(product.getState() != 0)
            throw new IllegalArgumentException("It is not a purchasable product");

        RLock lock = redissonClient.getLock(productBidKey + productId);

        try {
            // 최대 5초 동안 락 획득 시도, 최대 1초 동안 락 점유
            boolean available = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);

            if(!available) {
                System.out.println("purchase: failed to get a lock of productId " + productId);
                throw new InterruptedException("purchase: failed to get a lock of productId " + productId);
            }

            historyService.upsertBidHistory(memberId, product.getName(), product.getCategory());
            chatRoomRepository.save(
                    ChatRoom.builder()
                            .productId(product.getId())
                            .sellerId(product.getMemberId())
                            .bidderId(memberId)
                            .build()
            );

            product.setState(1);
            product.setBidPrice(product.getPrice());
            bidRepository.save(Bid.builder()
                    .productId(productId)
                    .memberId(memberId)
                    .price(product.getPrice())
                    .build()
            );

            upsertProductsToElastic(new ElasticProduct(product));
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public Page<ProductBriefResponse> suggestProducts(String memberId, Pageable pageable) throws IOException {
        final float BID_BOOST = 20F, SEARCH_BOOST = 10F;
        final int SEARCH_SIZE = 50;

        BidHistory bidHistory = historyService.getBidHistory(memberId);
        SearchHistory searchHistory = historyService.getSearchHistory(memberId);

        List<SearchRequest> requestList = new ArrayList<>();
        if(bidHistory != null)
            requestList.add(
                new SearchRequest.Builder()
                    .index("products")
                    .query(queryBuilder -> queryBuilder.moreLikeThis(getMoreLikeThisQuery(bidHistory.getKeyword(), BID_BOOST)))
                    .size(SEARCH_SIZE / 2)
                    .build()
            );
        if(searchHistory != null)
            requestList.add(
                new SearchRequest.Builder()
                    .index("products")
                    .query(queryBuilder -> queryBuilder.moreLikeThis(getMoreLikeThisQuery(searchHistory.getKeyword(), SEARCH_BOOST)))
                    .size(SEARCH_SIZE / 2)
                    .build()
            );
        if (requestList.isEmpty())
            requestList.add(
                    new SearchRequest.Builder()
                            .index("products")
                            .query(queryBuilder -> queryBuilder.match(term -> term.field("state").query(0)))
                            .size(SEARCH_SIZE)
                            .sort(so -> so
                                    .field(FieldSort.of(f -> f
                                            .field("deadline")
                                            .order(SortOrder.Asc))
                                    ))
                            .build()
            );

        List<Hit<ElasticProduct>> hits = new ArrayList<>();
        for (SearchRequest request: requestList) {
            SearchResponse<ElasticProduct> response = client.search(request, ElasticProduct.class);
            hits.addAll(response.hits().hits());
        }

        int found = hits.size();
        List<ProductBriefResponse> products = new ArrayList<>();
        for (int i = (int) pageable.getOffset(); i < found && i < pageable.getOffset() + pageable.getPageSize(); i++) {
            products.add(ProductBriefResponse.from(hits.get(i)));
        }

        return new PageImpl<>(products, pageable, found);
    }

    private MoreLikeThisQuery getMoreLikeThisQuery(List<String> keywords, float boost) {
        final int MIN_TERM_FREQ = 2, MIN_WORD_LENGTH = 0, MIN_DOC_FREQ = 7;

        List<Like> likeList = new ArrayList<>();
        for (String keyword: keywords) likeList.add(new Like.Builder().text(keyword).build());

        return QueryBuilders.moreLikeThis()
                .fields("product_name", "content")
                .like(likeList)
                .minTermFreq(MIN_TERM_FREQ)
                .minWordLength(MIN_WORD_LENGTH)
                .minDocFreq(MIN_DOC_FREQ)
                .boost(boost)
                .build();
    }

    @Scheduled(cron = "${redis.schedule.cron}")
    @Transactional
    public void updateExpiredState() {
        String instanceId = UUID.randomUUID().toString();
        boolean hasLock = redisTemplate.opsForValue().setIfAbsent(scheduleKey, instanceId, waitTime, TimeUnit.MINUTES);

        if (hasLock) {
            List<Product> products = productRepository.findAllByDeadlineBeforeAndState(LocalDateTime.now(), 0);
            if(products.size() == 0) return;
            RLock[] rLockList = new RLock[products.size()];

            for (int i = 0; i < products.size(); i++) {
                rLockList[i] = redissonClient.getLock(productBidKey + products.get(i).getId());
                products.get(i).setState(2);
            }

            RLock multiLock = redissonClient.getMultiLock(rLockList);

            try {
                boolean available = multiLock.tryLock(waitTime * 2, leaseTime * 2, TimeUnit.SECONDS);

                if(!available) {
                    System.out.println("updateExpired: failed to get a multiLock");
                    throw new InterruptedException("updateExpired: failed to get a multiLock");
                }

                productRepository.saveAll(products);
                bulkUpsertProductsToElastic(products.stream().map(product -> new ElasticProduct(product)).toList());
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            } finally {
                multiLock.unlock();
            }
        }
    }

    @Transactional
    public void upsertProductsToElastic(ElasticProduct elasticProduct) throws IOException {
        ProductUpsertSchedule schedule = productUpsertScheduleRepository.findById(scheduleId)
                .orElse(productUpsertScheduleRepository.save(new ProductUpsertSchedule()));

        List<ElasticProduct> productList = schedule.getProductList();
        productList.add(elasticProduct);

//        System.out.println("현재 스케줄");
//        for (ElasticProduct product: schedule.getProductList()) System.out.println(product.getProduct_id() + " / " + product.getProduct_name());

        if(productList.size() < upsertScheduleSize) {
            productUpsertScheduleRepository.save(schedule);
            return;
        }

        BulkRequest.Builder requestBuilder = new BulkRequest.Builder();

        for (ElasticProduct product: productList) {
            requestBuilder.operations(op -> op
                    .index(idx -> idx
                            .index("products")
                            .id(product.getProduct_id().toString())
                            .document(product)
                    )
            );
        }

        BulkResponse response = client.bulk(requestBuilder.build());

        if (response.errors()) {
            for (BulkResponseItem item: response.items()) {
                if (item.error() != null) {
                    System.out.println(item.error().reason());
                }
            }
            throw new IOException("bulk api error");
        }

        productList.clear();
        productUpsertScheduleRepository.save(schedule);

//        System.out.println("남은 스케줄");
//        for (ElasticProduct product: schedule.getProductList()) System.out.println(product.getProduct_id() + " / " + product.getProduct_name());
    }

    @Transactional
    public void bulkUpsertProductsToElastic(List<ElasticProduct> elasticProducts) throws IOException {
        BulkRequest.Builder requestBuilder = new BulkRequest.Builder();

        for (ElasticProduct product: elasticProducts) {
            requestBuilder.operations(op -> op
                    .index(idx -> idx
                            .index("products")
                            .id(product.getProduct_id().toString())
                            .document(product)
                    )
            );
        }

        BulkResponse response = client.bulk(requestBuilder.build());

        if (response.errors()) {
            for (BulkResponseItem item: response.items()) {
                if (item.error() != null) {
                    System.out.println(item.error().reason());
                }
            }
            throw new IOException("bulk api error");
        }
    }
}
