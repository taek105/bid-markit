package com.capstone.bidmarkit.controller;

import com.capstone.bidmarkit.dto.SendChatRequest;
import com.capstone.bidmarkit.redis.RedisPublisher;
import com.capstone.bidmarkit.service.ChatService;
import com.capstone.bidmarkit.service.PushService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChatService chatService;
    private final PushService pushService;

    @MessageMapping("/chat/message")
    public void message(SendChatRequest request) {
        redisPublisher.publish("bidmarKitChatRoom", request);

        pushService.pushAlarm(chatService.setChatPushAlarmRequest(request));

        chatService.save(request);
    }
}
