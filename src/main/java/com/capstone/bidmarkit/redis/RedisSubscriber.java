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
            // redis 에서 발행된 데이터를 받아 역직렬화
            String publishMessage = (String) rT.getStringSerializer().deserialize(message.getBody());

            SendChatRequest sendChatRequest = objectMapper.readValue(publishMessage, SendChatRequest.class);

            // Websocket 구독자에게 채팅 메시지 전송
            mT.convertAndSend("/sub/chat/room/" + sendChatRequest.getChatRoomId(), sendChatRequest);
        } catch (Exception e) {
            System.out.println("onMessage Error: " + e.getMessage());
        }
    }
}
