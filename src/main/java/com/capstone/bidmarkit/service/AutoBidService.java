package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.AutoBid;
import com.capstone.bidmarkit.domain.Bid;
import com.capstone.bidmarkit.domain.Product;
import com.capstone.bidmarkit.dto.AddAutoBidRequest;
import com.capstone.bidmarkit.dto.ElasticProduct;
import com.capstone.bidmarkit.dto.PushAlarmRequest;
import com.capstone.bidmarkit.repository.AutoBidRepository;
import com.capstone.bidmarkit.repository.BidRepository;
import com.capstone.bidmarkit.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class AutoBidService {
    private final AutoBidRepository autoBidRepository;
    private final BidRepository bidRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final HistoryService historyService;
    private final RedissonClient redissonClient;
    private final PushService pushService;

    @Value("${redis.product-bid.key}")
    private String productBidKey;

    @Value("${redis.product-bid.wait-time}")
    private int waitTime;

    @Value("${redis.product-bid.lease-time}")
    private int leaseTime;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public AutoBid save(String memberId, AddAutoBidRequest dto) {
        // 입찰 대상 상품 미검색 시, 예외 발생
        Product product = productRepository.findById(dto.getProductId()).orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // 상품 상태가 판매 중이 아닐 경우, 예외 발생
        if(product.getState() != 0)
            throw new IllegalArgumentException("It is not a biddable product");

        // 본인 상품을 자동 입찰 시도 시, 예외 발생
        if(product.getMemberId().equals(memberId))
            throw new IllegalArgumentException("You can't bid for your product yourself.");
        
        int minBidPrice = product.getBidPrice() + minBidPrice(product.getBidPrice());
        minBidPrice = product.getPrice() < minBidPrice ? product.getPrice() : minBidPrice;

        // 입찰 대상의 최소 상회 입찰가보다 낮은 가격으로 자동 입찰 시도 시, 예외 발생
        if(minBidPrice > dto.getCeilingPrice())
            throw new IllegalArgumentException("Price to set AutoBid is not enough");

        Bid currentBid = bidRepository.findTopByProductIdOrderByPriceDesc(dto.getProductId()).orElse(null);

        RLock lock = redissonClient.getLock(productBidKey + product.getId());

        AutoBid newAutoBid;

        try {
            boolean available = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);

            if(!available) throw new InterruptedException("autoBid: failed to get a lock of productId " + product.getId());

            AutoBid currentAutoBid = autoBidRepository.findByProductId(dto.getProductId());

            newAutoBid = AutoBid.builder()
                    .productId(dto.getProductId())
                    .ceilingPrice(dto.getCeilingPrice())
                    .memberId(memberId)
                    .build();

            // 상품 입찰 내역 저장
            historyService.upsertBidHistory(memberId, product.getName(), product.getCategory());

            // 기존 자동 입찰 설정이 없을 경우,
            if(currentAutoBid == null) {
                autoBidRepository.save(newAutoBid);
                // 최고가 입찰 내역이 존재하고, 해당 입찰을 진행한 멤버가 지금 자동 입찰을 시도하는 멤버가 아닐 경우, 상회 입찰을 진행
                if(currentBid != null && !currentBid.getMemberId().equals(memberId)) {
                    bidRepository.save(
                            Bid.builder()
                                    .productId(dto.getProductId())
                                    .memberId(memberId)
                                    .price(minBidPrice)
                                    .build()
                    );
                    pushService.pushAlarm(PushAlarmRequest.builder()
                            .productName(product.getName())
                            .imgURL(product.getImages().get(0).getImgUrl())
                            .memberId(currentBid.getMemberId())
                            .type(3)
                            .build()
                    );
                    // 즉시구매가로 입찰했을 경우, 즉시구매처리
                    if(product.getPrice().equals(minBidPrice)) {
                        product.setState(1);
                        product.setBidPrice(product.getPrice());
                        autoBidRepository.delete(newAutoBid);
                    } else product.setBidPrice(minBidPrice);
                }
                productService.upsertProductsToElastic(new ElasticProduct(product));
                return newAutoBid;
            }

            int minAutoBidPrice = currentAutoBid.getCeilingPrice() + minBidPrice(currentAutoBid.getCeilingPrice());
            minAutoBidPrice = minAutoBidPrice > product.getPrice() ? product.getPrice() : minAutoBidPrice;

            if(minAutoBidPrice <= dto.getCeilingPrice()) {
                autoBidRepository.delete(currentAutoBid);
                pushService.pushAlarm(PushAlarmRequest.builder()
                        .productName(product.getName())
                        .imgURL(product.getImages().get(0).getImgUrl())
                        .memberId(currentAutoBid.getMemberId())
                        .type(4)
                        .build()
                );
                autoBidRepository.save(newAutoBid);
                bidRepository.save(
                        Bid.builder()
                                .productId(dto.getProductId())
                                .memberId(memberId)
                                .price(minAutoBidPrice)
                                .build()
                );
                product.setBidPrice(minAutoBidPrice);
                productService.upsertProductsToElastic(new ElasticProduct(product));
                return newAutoBid;
            }
            minAutoBidPrice = dto.getCeilingPrice() + minBidPrice(dto.getCeilingPrice());
            minAutoBidPrice = currentAutoBid.getCeilingPrice() < minAutoBidPrice ? currentAutoBid.getCeilingPrice() : minAutoBidPrice;

            bidRepository.save(
                    Bid.builder()
                            .productId(dto.getProductId())
                            .memberId(currentAutoBid.getMemberId())
                            .price(minAutoBidPrice)
                            .build()
            );
            pushService.pushAlarm(PushAlarmRequest.builder()
                    .productName(product.getName())
                    .imgURL(product.getImages().get(0).getImgUrl())
                    .memberId(memberId)
                    .type(4)
                    .build()
            );
            historyService.upsertBidHistory(currentAutoBid.getMemberId(), product.getName(), product.getCategory());
            product.setBidPrice(minAutoBidPrice);
            productService.upsertProductsToElastic(new ElasticProduct(product));
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }

        return newAutoBid;
    }

    public int minBidPrice(int currentPrice) {
        if (currentPrice < 10000) return 100;
        if (currentPrice < 50000) return 1000;
        if (currentPrice < 100000) return 2500;
        if (currentPrice < 500000) return 5000;
        return 10000;
    }
}
