package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.AutoBid;
import com.capstone.bidmarkit.domain.Bid;
import com.capstone.bidmarkit.domain.Product;
import com.capstone.bidmarkit.domain.QBid;
import com.capstone.bidmarkit.dto.AddBidRequest;
import com.capstone.bidmarkit.dto.BidResponse;
import com.capstone.bidmarkit.repository.AutoBidRepository;
import com.capstone.bidmarkit.repository.BidRepository;
import com.capstone.bidmarkit.repository.ProductRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BidService {
    private final BidRepository bidRepository;
    private final AutoBidRepository autoBidRepository;
    private final ProductRepository productRepository;
    private final HistoryService historyService;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Bid save(String memberId, AddBidRequest dto) {
        // 입찰 대상 상품 미검색 시, 예외 발생
        Product product = productRepository.findById(dto.getProductId()).orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // 상품 상태가 판매 중이 아닐 경우, 예외 발생
        if(product.getState() != 0)
            throw new IllegalArgumentException("It is not a bidable product");

        // 본인 상품을 자동 입찰 시도 시, 예외 발생
        if(product.getMemberId().equals(memberId))
            throw new IllegalArgumentException("You can't bid for your product yourself.");

        // 입찰 대상의 최소 상회 입찰가보다 낮은 가격으로 입찰 시도 시, 예외 발생
        if(product.getBidPrice() + minBidPrice(product.getBidPrice()) >= dto.getPrice()) throw new IllegalArgumentException("Price to bid is not enough");

        // 상품 입찰 내역 저장
        historyService.upsertBidHistory(memberId, product.getName(), product.getCategory());

        // 입찰 정보 저장
        Bid newBid = bidRepository.save(
                Bid.builder()
                        .productId(dto.getProductId())
                        .memberId(memberId)
                        .price(dto.getPrice() > product.getPrice() ? product.getPrice() : dto.getPrice())
                        .build()
        );
        product.setBidPrice(newBid.getPrice());

        AutoBid autoBid = autoBidRepository.findByProductId(dto.getProductId());

        // 즉시구매가와 같거나 높은 가격으로 상회 입찰을 했을 경우, 즉시구매처리
        if(newBid.getPrice().equals(product.getPrice())) {
            if(autoBid != null) autoBidRepository.delete(autoBid);
            product.setState(1);
            return newBid;
        }

        // 자동 입찰이 설정되었을 경우, 자동 입찰 진행
        if(autoBid != null) {
            int calNewPrice = newBid.getPrice() + minBidPrice(newBid.getPrice());
            // 자동 입찰 설정 금액이 최소 상회 입찰가보다 작다면, 자동 입찰 설정 해제 / 크다면, 자동 입찰 진행
            if(autoBid.getCeilingPrice() < calNewPrice) {
                autoBidRepository.delete(autoBid);
                product.setBidPrice(newBid.getPrice());
            } else {
                Bid newBidByAuto = bidRepository.save(
                        Bid.builder()
                                .productId(autoBid.getProductId())
                                .memberId(autoBid.getMemberId())
                                .price(calNewPrice > product.getPrice() ? product.getPrice() : calNewPrice)
                                .build()
                );
                // 즉시구매가와 같거나 높은 가격으로 상회 입찰을 했을 경우, 즉시구매처리
                if(newBidByAuto.getPrice().equals(product.getPrice())) {
                    product.setState(1);
                }
                product.setBidPrice(newBidByAuto.getPrice());
            }
        }
        return newBid;
    }

    public List<BidResponse> findAllByProductId(int productId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QBid bid = QBid.bid;

        List<BidResponse> results = queryFactory
                .select(Projections.constructor(BidResponse.class, bid.memberId, bid.price, bid.createdAt))
                .from(bid)
                .where(bid.productId.eq(productId))
                .orderBy(bid.price.desc())
                .fetch();

        return results;
    }

    public int minBidPrice(int currentPrice) {
        if (currentPrice < 10000) return 100;
        if (currentPrice < 50000) return 1000;
        if (currentPrice < 100000) return 2500;
        if (currentPrice < 500000) return 5000;
        return 10000;
    }
}
