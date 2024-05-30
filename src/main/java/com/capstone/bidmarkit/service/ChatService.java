package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.*;
import com.capstone.bidmarkit.dto.SendChatRequest;
import com.capstone.bidmarkit.repository.ChatMessageRepository;
import com.capstone.bidmarkit.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatMessage save(SendChatRequest request) {

        chatRoomRepository.updateUpdatedAt(request.getChatRoomId(), LocalDateTime.now(ZoneId.of("Asia/Seoul")));

        return chatMessageRepository.save(
                ChatMessage.builder()
                        .chatRoomId(request.getChatRoomId())
                        .senderId(request.getSenderId())
                        .content(request.getContent())
                        .build()
        );
    }

}

