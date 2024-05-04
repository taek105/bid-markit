package com.capstone.bidmarkit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddBidRequest {
    private int id;
    private int productId;
    private String memberId;
    private int price;
}