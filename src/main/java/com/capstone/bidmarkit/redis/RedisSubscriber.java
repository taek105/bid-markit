package com.capstone.bidmarkit.redis;

import com.capstone.bidmarkit.dto.SendChatRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> rT;
    private final SimpMessageSendingOperations mT;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = rT.getStringSerializer().deserialize(message.getBody());

            SendChatRequest sendChatRequest = objectMapper.readValue(publishMessage, SendChatRequest.class);

            mT.convertAndSend("/sub/chatRooms/" + sendChatRequest.getChatRoomId(), sendChatRequest);
        } catch (Exception e) {
            System.out.println("onMessage Error: " + e.getMessage());
        }
    }
}
