package com.capstone.bidmarkit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomListResponse {
    private int id;
    private String sellerId, bidderId;
    private String thumbnail, productName;
    private String lastMessage;
    private LocalDateTime updatedAt;
}
