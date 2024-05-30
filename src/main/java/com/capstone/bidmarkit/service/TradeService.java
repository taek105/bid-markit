package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.Trade;
import com.capstone.bidmarkit.dto.AddTradeRequest;
import com.capstone.bidmarkit.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TradeService {

    private final TradeRepository tradeRepository;

    public Trade save(AddTradeRequest request) {
        return tradeRepository.save(
                Trade.builder()
                        .product(request.getProduct())
                        .buyerId(request.getBuyerId())
                        .price(request.getPrice())
                        .build()
        );
    }
}
