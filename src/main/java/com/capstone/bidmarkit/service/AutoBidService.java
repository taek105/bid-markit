package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.AutoBid;
import com.capstone.bidmarkit.dto.AddAutoBidRequest;
import com.capstone.bidmarkit.repository.AutoBidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AutoBidService {
    private final AutoBidRepository autoBidRepository;

    public int save(AddAutoBidRequest dto) {
        return autoBidRepository.save(
                AutoBid.builder()
                        .id(dto.getId())
                        .productId(dto.getProductId())
                        .memberId(dto.getMemberId())
                        .ceilingPrice(dto.getCeilingPrice())
                        .build()
        ).getId();
    }
}
