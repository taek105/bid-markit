package com.capstone.bidmarkit.redis;

import com.capstone.bidmarkit.dto.SendChatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher{

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void publish(String topic, SendChatRequest message) {
        redisTemplate.convertAndSend(topic, message);
    }
}
