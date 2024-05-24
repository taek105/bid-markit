package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.*;
import com.capstone.bidmarkit.dto.SendChatRequest;
import com.capstone.bidmarkit.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage save(SendChatRequest request) {

        /*
         업데이트 쿼리 하나 날려서 ChatRoom에 updated_at 이벤트 발생하게 해야함
        */

        return chatMessageRepository.save(
                ChatMessage.builder()
                        .senderId(request.getSenderId())
                        .content(request.getContent())
                        .build()
        );
    }

}

