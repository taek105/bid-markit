package com.capstone.bidmarkit.dto;

import com.capstone.bidmarkit.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddTradeRequest {
    private Product product;
    private String buyerId;
    private int price;
}
