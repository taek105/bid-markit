package com.capstone.bidmarkit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetPurchaseResponse {
    String thumbnail, productName;
    Integer productId, price;
}