package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.AutoBid;
import com.capstone.bidmarkit.domain.Bid;
import com.capstone.bidmarkit.domain.Member;
import com.capstone.bidmarkit.domain.Product;
import com.capstone.bidmarkit.dto.AddBidRequest;
import com.capstone.bidmarkit.repository.BidRepository;
import com.capstone.bidmarkit.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BidService {
    private final BidRepository bidRepository;
    private final ProductRepository productRepository;

    public int save(AddBidRequest dto) {
        Product product = productRepository.findById(dto.getProductId()).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.setBidPrice(dto.getPrice()); // 혹은 다른 방법으로 bid_price 수정
        productRepository.save(product);

        return bidRepository.save(
                Bid.builder()
                        .id(dto.getId())
                        .productId(dto.getProductId())
                        .memberId(dto.getMemberId())
                        .price(dto.getPrice())
                        .build()
        ).getId();
    }
    public List<Bid> findAllByProductId(int productId) {
        List<Bid> bids = bidRepository.findAllByProductId(productId);
        if (bids.isEmpty()) {
            throw new IllegalArgumentException("No bids found for product ID: " + productId);
        }

        return bids;
    }
}
