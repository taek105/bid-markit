package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.*;

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
import org.springframework.dao.DuplicateKeyException;
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

        if (chatRoomRepository.existsByProductIdAndBidderId(request.getProductId(), bidderId))
            throw new DuplicateKeyException("ChatRoom already exists.");

        return chatRoomRepository.save(
                ChatRoom.builder()
                        .productId(request.getProductId())
                        .sellerId(request.getSellerId())
                        .bidderId(bidderId)
                        .build()
        );
    }

    public Page<ChatRoomListResponse> findAllRoomByMemberId(String memberId, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        QChatRoom chatRoom = QChatRoom.chatRoom;
        QProductImg productImg = QProductImg.productImg;
        QProduct product = QProduct.product;

        List<ChatRoomListResponse> results = queryFactory
                .select(Projections.constructor
                            (ChatRoomListResponse.class,
                                chatRoom.id,
                                chatRoom.sellerId,
                                chatRoom.bidderId,
                                productImg.imgUrl,
                                product.name,
                                chatRoom.updatedAt
                            )
                        )
                .from(chatRoom)
                .leftJoin(productImg).on(chatRoom.productId.eq(productImg.product.id)
                        .and(productImg.isThumbnail.isTrue()))
                .leftJoin(product).on(chatRoom.productId.eq(product.id))
                .where(chatRoom.sellerId.eq(memberId).or(chatRoom.bidderId.eq(memberId)))
                .orderBy(chatRoom.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(chatRoom.count())
                .from(chatRoom)
                .where(chatRoom.sellerId.eq(memberId).or(chatRoom.bidderId.eq(memberId)))
                .fetchCount();

        return PageableExecutionUtils.getPage(results, pageable, () -> total);
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

        int productId = chatRoomRepository.findProductIdByRoomId(roomId);
        List<String> imgUrls = productImgRepository.findImgUrlsByProductId(productId);
        String productName = productRepository.findProductNameById(productId);
        int price = productRepository.findPriceById(productId);

        return new ChatRoomDetailResponse(imgUrls.get(0), productName, price, results);
    }

    public UpdateCheckResponse updateCheck(String memberId, int roomId, Byte checkType) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId);

        if (memberId.equals(chatRoom.getSellerId())) {
            chatRoom.setSellerCheck(checkType);
            chatRoomRepository.save(chatRoom);
            return new UpdateCheckResponse(chatRoom.getSellerCheck(), chatRoom.getBidderCheck());
        }
        else if (memberId.equals(chatRoom.getBidderId())) {
            chatRoom.setBidderCheck(checkType);
            chatRoomRepository.save(chatRoom);
            return new UpdateCheckResponse(chatRoom.getSellerCheck(), chatRoom.getBidderCheck());
        }
        else
            throw new IllegalArgumentException("not matched sellerId or bidderId");
    }
}
