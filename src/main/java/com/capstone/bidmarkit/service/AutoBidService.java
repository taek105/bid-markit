package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.AutoBid;
import com.capstone.bidmarkit.domain.Bid;
import com.capstone.bidmarkit.domain.Product;
import com.capstone.bidmarkit.dto.AddAutoBidRequest;
import com.capstone.bidmarkit.dto.AddBidRequest;
import com.capstone.bidmarkit.repository.AutoBidRepository;
import com.capstone.bidmarkit.repository.BidRepository;
import com.capstone.bidmarkit.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AutoBidService {
    private final AutoBidRepository autoBidRepository;
    private final BidRepository bidRepository;
    private final ProductRepository productRepository;
    private final TokenService tokenService;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public AutoBid save(String token, AddAutoBidRequest dto) {
        // 입찰 대상 상품 미검색 시, 예외 발생
        Product product = productRepository.findById(dto.getProductId()).orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // 상품 상태가 판매 중이 아닐 경우, 예외 발생
        if(product.getState() != 0)
            throw new IllegalArgumentException("It is not a biddable product");

        String requestMemberId = tokenService.getMemberId(token);

        // 본인 상품을 자동 입찰 시도 시, 예외 발생
        if(product.getMemberId() == requestMemberId)
            throw new IllegalArgumentException("You can't bid for your product yourself.");
        
        int minBidPrice = product.getBidPrice() + minBidPrice(product.getBidPrice());
        minBidPrice = product.getPrice() < minBidPrice ? product.getPrice() : minBidPrice;

        // 입찰 대상의 최소 상회 입찰가보다 낮은 가격으로 자동 입찰 시도 시, 예외 발생
        if(minBidPrice >= dto.getCeilingPrice())
            throw new IllegalArgumentException("Price to set AutoBid is not enough");

        AutoBid currentAutoBid = autoBidRepository.findByProductId(dto.getProductId());

        AutoBid newAutoBid = AutoBid.builder()
                .productId(dto.getProductId())
                .ceilingPrice(dto.getCeilingPrice())
                .memberId(requestMemberId)
                .build();

        // 기존 자동 입찰 설정이 없을 경우,
        if(currentAutoBid == null) {
            autoBidRepository.save(newAutoBid);
            Optional<Bid> currentBid = bidRepository.findTopByProductIdOrderByPriceDesc(dto.getProductId());
            // 최고가 입찰 내역이 존재하고, 해당 입찰을 진행한 멤버가 지금 자동 입찰을 시도하는 멤버가 아닐 경우, 상회 입찰을 진행
            if(currentBid.isPresent() && currentBid.get().getMemberId() != requestMemberId) {
                bidRepository.save(
                        Bid.builder()
                        .productId(dto.getProductId())
                                .memberId(requestMemberId)
                                .price(minBidPrice)
                                .build()
                );
                // 즉시구매가로 입찰했을 경우, 즉시구매처리
                if(product.getPrice().equals(minBidPrice)) {
                    product.setState(1);
                    product.setBidPrice(product.getPrice());
                    autoBidRepository.delete(newAutoBid);
                } else
                    product.setBidPrice(minBidPrice);
            }
            return newAutoBid;
        }

        int minAutoBidPrice = currentAutoBid.getCeilingPrice() + minBidPrice(currentAutoBid.getCeilingPrice());
        minAutoBidPrice = minAutoBidPrice > product.getPrice() ? product.getPrice() : minAutoBidPrice;

        if(minAutoBidPrice <= dto.getCeilingPrice()) {
            autoBidRepository.delete(currentAutoBid);
            autoBidRepository.save(newAutoBid);
            bidRepository.save(
                    Bid.builder()
                    .productId(dto.getProductId())
                    .memberId(requestMemberId)
                    .price(minAutoBidPrice)
                    .build()
            );
            product.setBidPrice(minAutoBidPrice);
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
