package com.capstone.bidmarkit.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatRoomDetailResponse {
    private String thumbnail, productName;
    private String sellerId;
    private Byte sellerCheck;
    private String bidderId;
    private Byte bidderCheck;
    private int price;
    private List<ChatLogResponse> log;

    public ChatRoomDetailResponse(String thumbnail, String productName, String sellerId, String bidderId, int price, Byte sellerCheck, Byte bidderCheck, List<ChatLogResponse> log) {
        this.thumbnail = thumbnail;
        this.productName = productName;
        this.price = price;
        this.sellerId = sellerId;
        this.bidderId = bidderId;
        this.sellerCheck = sellerCheck;
        this.bidderCheck = bidderCheck;
        this.log = log;
    }
}