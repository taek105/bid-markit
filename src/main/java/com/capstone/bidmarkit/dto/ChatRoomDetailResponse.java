package com.capstone.bidmarkit.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatRoomDetailResponse {
    private String thumbnail, productName;
    private int price;
    private List<ChatLogResponse> log;

    public ChatRoomDetailResponse(String thumbnail, String productName, int price, List<ChatLogResponse> log) {
        this.thumbnail = thumbnail;
        this.productName = productName;
        this.price = price;
        this.log = log;
    }
}