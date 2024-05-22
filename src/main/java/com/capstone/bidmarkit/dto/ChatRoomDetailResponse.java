package com.capstone.bidmarkit.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatRoomDetailResponse {
    private String thumbnail, productName;
    private List<ChatLogResponse> log;

    public ChatRoomDetailResponse(String thumbnail, String productName, List<ChatLogResponse> log) {
        this.thumbnail = thumbnail;
        this.productName = productName;
        this.log = log;
    }
}