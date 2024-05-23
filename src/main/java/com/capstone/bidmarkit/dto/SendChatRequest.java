package com.capstone.bidmarkit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SendChatRequest {

    private int chatRoomId;
    private String senderId;
    private String content;

}