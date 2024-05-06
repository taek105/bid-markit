package com.capstone.bidmarkit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddAutoBidRequest {
    private int productId;
    private int ceilingPrice;
}
