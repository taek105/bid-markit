package com.capstone.bidmarkit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddAutoBidRequest {
    private int id;
    private int productId;
    private String memberId;
    private int ceilingPrice;
}
