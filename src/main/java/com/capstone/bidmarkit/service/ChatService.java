package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.*;
import com.capstone.bidmarkit.dto.PushAlarmRequest;
import com.capstone.bidmarkit.dto.SendChatRequest;
import com.capstone.bidmarkit.repository.ChatMessageRepository;
import com.capstone.bidmarkit.repository.ChatRoomRepository;
import com.capstone.bidmarkit.repository.ProductImgRepository;
import com.capstone.bidmarkit.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;
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

    public PushAlarmRequest setChatPushAlarmRequest(SendChatRequest request) {
        String pushedId;
        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId());
        if ( request.getSenderId().equals(chatRoom.getBidderId()) )
            pushedId = chatRoom.getSellerId();
        else
            pushedId = chatRoom.getBidderId();
        Product product = productRepository.findById(chatRoom.getProductId()).orElseThrow();

        List<String> imgUrls = productImgRepository.findImgUrlsByProductId(product.getId());

        return new PushAlarmRequest(
                pushedId,
                product.getName(),
                imgUrls.get(0),
                request.getContent(),
                5);
    }
}

