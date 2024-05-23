package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.ChatRoom;
import com.capstone.bidmarkit.domain.QChatMessage;
import com.capstone.bidmarkit.domain.QChatRoom;

import com.capstone.bidmarkit.dto.*;
import com.capstone.bidmarkit.repository.ChatRoomRepository;
import com.capstone.bidmarkit.repository.ProductImgRepository;
import com.capstone.bidmarkit.repository.ProductRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;
    @PersistenceContext
    private EntityManager entityManager;

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoom save(String bidderId, AddChatRoomRequest request) {
        ChatRoom newChatRoom = chatRoomRepository.save(
                ChatRoom.builder()
                        .productId(request.getProductId())
                        .sellerId(request.getSellerId())
                        .bidderId(bidderId)
                        .build()
        );
        return newChatRoom;
    }

    public Page<ChatRoomListResponse> findAllRoomByMemberId(String memberId, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QChatRoom chatRoom = QChatRoom.chatRoom;

        List<ChatRoomListResponse> results = queryFactory
                .select(Projections.constructor(ChatRoomListResponse.class, chatRoom.id, chatRoom.sellerId, chatRoom.bidderId, chatRoom.updatedAt))
                .from(chatRoom)
                .where(chatRoom.sellerId.eq(memberId).or(chatRoom.bidderId.eq(memberId))) // sellerId 또는 bidderId가 memberId와 일치하는 경우
                .orderBy(chatRoom.updatedAt.desc())
                .fetch();

        return PageableExecutionUtils.getPage(results, pageable, queryFactory.select(chatRoom.count()).from(chatRoom)::fetchCount);
    }
    public ChatRoomDetailResponse findChatDetailsByRoomId(int roomId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QChatMessage chatMessage = QChatMessage.chatMessage;

        List<ChatLogResponse> results = queryFactory
                .select(Projections.constructor(ChatLogResponse.class, chatMessage.senderId, chatMessage.content, chatMessage.createdAt))
                .from(chatMessage)
                .where(chatMessage.chatRoomId.eq(roomId))
                .orderBy(chatMessage.createdAt.desc())
                .limit(100)
                .fetch();

        int productId = chatRoomRepository.findProductIdById(roomId);
        List<String> imgUrls = productImgRepository.findImgUrlsByProductId(productId);
        String productName = productRepository.findProductNameById(productId);

        return new ChatRoomDetailResponse(imgUrls.get(0), productName, results);
    }
}
