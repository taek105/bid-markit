package com.capstone.bidmarkit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatRoomListResponse {
    private int id;
    private String sellerId;
    private String bidderId;
    private LocalDateTime updatedAt;

}
