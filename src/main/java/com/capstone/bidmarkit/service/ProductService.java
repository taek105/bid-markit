package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.*;
import com.capstone.bidmarkit.dto.AddProductRequest;
import com.capstone.bidmarkit.dto.ProductBriefResponse;
import com.capstone.bidmarkit.dto.ProductDetailResponse;
import com.capstone.bidmarkit.repository.BidRepository;
import com.capstone.bidmarkit.repository.ProductImgRepository;
import com.capstone.bidmarkit.repository.ProductRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;
    private final BidRepository bidRepository;
    private final TokenService tokenService;

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public int save(AddProductRequest dto) {
        productImgRepository.saveAll(dto.getImages());
        return productRepository.save(
                Product.builder()
                        .id(dto.getId())
                        .memberId(dto.getMemberId())
                        .name(dto.getName())
                        .category(dto.getCategory())
                        .content(dto.getContent())
                        .initPrice(dto.getInitPrice())
                        .price(dto.getPrice())
                        .deadline(dto.getDeadline())
                        .build()
        ).getId();
    }

    public Page<ProductBriefResponse> findAllOrderByDeadlineAsc(Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QProduct product = QProduct.product;
        QProductImg productImg = QProductImg.productImg;

        List<ProductBriefResponse> results = queryFactory
                .select(Projections.constructor(ProductBriefResponse.class, productImg.imgUrl, product.name, product.bidPrice, product.price, product.deadline))
                .from(product)
                .leftJoin(product.images, productImg)
                .groupBy(product.id, productImg.imgUrl)
                .where(productImg.isThumbnail.isTrue())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(results, pageable, queryFactory.select(product.count()).from(product)::fetchCount);
    }

    public ProductDetailResponse findDetail(int productId) {
        ProductDetailResponse res = new ProductDetailResponse();

        Product a = productRepository.findDetailById(productId);

        res.setImages(productImgRepository.findByProductId(productId)
                .stream()
                .map(ProductImg::getImgUrl)
                .collect(Collectors.toList()));
        res.setProductName(a.getName());
        res.setBidPrice(a.getBidPrice());
        res.setInitPrice(a.getInitPrice());
        res.setPrice(a.getPrice());
        res.setDeadline(a.getDeadline());
        res.setSellerName(a.getMemberId());
        res.setContent(a.getContent());

        return res;
    }

    @Transactional
    public void purchaseProduct(String token, int productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // 상품 상태가 판매 중이 아닐 경우, 예외 발생
        if(product.getState() != 0)
            throw new IllegalArgumentException("It is not a purchasable product");

        product.setState(1);
        product.setBidPrice(product.getPrice());
        bidRepository.save(Bid.builder()
                .productId(productId)
                .memberId(tokenService.getMemberId(token))
                .price(product.getPrice())
                .build()
        );
    }
}
