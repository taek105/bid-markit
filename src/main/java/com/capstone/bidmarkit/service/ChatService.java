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
        return chatMessageRepository.save(
                ChatMessage.builder()
                        .senderId(request.getSenderId())
                        .content(request.getContent())
                        .build()
        );
    }

}

