package com.capstone.bidmarkit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddChatRoomRequest {
    private Integer productId;
    private String sellerId;
}
