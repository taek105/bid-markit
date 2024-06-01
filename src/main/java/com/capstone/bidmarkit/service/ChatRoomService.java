package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.*;

import com.capstone.bidmarkit.dto.*;
import com.capstone.bidmarkit.repository.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
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

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final ProductImgRepository productImgRepository;
    private final TradeRepository tradeRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager entityManager;

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
        QChatMessage chatMessage = QChatMessage.chatMessage;

        QChatMessage subChatMessage = new QChatMessage("subChatMessage");
        List<ChatRoomListResponse> results = queryFactory
                .select(Projections.constructor(
                        ChatRoomListResponse.class,
                        chatRoom.id,
                        chatRoom.sellerId,
                        chatRoom.bidderId,
                        productImg.imgUrl,
                        product.name,
                        chatMessage.content,
                        chatRoom.updatedAt
                ))
                .from(chatRoom)
                .leftJoin(productImg).on(chatRoom.productId.eq(productImg.product.id)
                        .and(productImg.isThumbnail.isTrue()))
                .leftJoin(product).on(chatRoom.productId.eq(product.id))
                .leftJoin(chatMessage).on(chatRoom.id.eq(chatMessage.chatRoomId)
                        .and(chatMessage.createdAt.eq(
                                JPAExpressions.select(subChatMessage.createdAt.max())
                                        .from(subChatMessage)
                                        .where(subChatMessage.chatRoomId.eq(chatRoom.id))
                        )))
                .where(chatRoom.sellerId.eq(memberId).or(chatRoom.bidderId.eq(memberId)))
                .orderBy(chatRoom.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        for (int i = 0; i < results.size()-1; i++) {
            ChatRoomListResponse item = results.get(i);
            if ( item.getId() == results.get(i+1).getId() )  results.remove(i);
        }

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
                .orderBy(chatMessage.createdAt.asc())
                .limit(100)
                .fetch();

        ChatRoom chatRoom = chatRoomRepository.findById(roomId);
        Product product = productRepository.findById(chatRoom.getProductId()).orElseThrow();

        List<String> imgUrls = productImgRepository.findImgUrlsByProductId(product.getId());

        return new ChatRoomDetailResponse(imgUrls.get(0), product.getName(), chatRoom.getSellerId(), chatRoom.getBidderId(), product.getPrice(), chatRoom.getSellerCheck(), chatRoom.getBidderCheck(), results);
    }

    public UpdateCheckResponse updateCheck(String memberId, int roomId, Byte checkType) throws IOException {
        if ( checkType != 2 && checkType != 1) throw new IllegalArgumentException("invalid checkType");

        ChatRoom chatRoom = chatRoomRepository.findById(roomId);

        if (memberId.equals(chatRoom.getSellerId())) chatRoom.setSellerCheck(checkType);
        else if (memberId.equals(chatRoom.getBidderId())) chatRoom.setBidderCheck(checkType);
        else throw new IllegalArgumentException("not matched sellerId or bidderId");

        Product found = productRepository.findById(chatRoom.getProductId()).orElseThrow(
                () -> new IllegalArgumentException("no found product")
        );

        if ( chatRoom.getBidderCheck() == 2 && chatRoom.getSellerCheck() == 2 ) {
            updateProductState(found, 3);
            tradeRepository.save(
                    Trade.builder()
                            .product(found)
                            .buyerId(chatRoom.getSellerId())
                            .price(found.getBidPrice())
                            .build()
            );
        } else if (chatRoom.getBidderCheck() > 0 && chatRoom.getSellerCheck() > 0) {
            updateProductState(found, 2);
            if ( chatRoom.getSellerCheck() == 2 ) {
                memberRepository.incrCancelSale(chatRoom.getSellerId());
            }
            else if ( chatRoom.getBidderCheck() == 2 ){
                memberRepository.incrCancelPurchase(chatRoom.getBidderId());
            }
            else {
                memberRepository.incrCancelSale(chatRoom.getSellerId());
                memberRepository.incrCancelPurchase(chatRoom.getBidderId());
            }
        }

        chatRoomRepository.save(chatRoom);
        return new UpdateCheckResponse(chatRoom.getSellerCheck(), chatRoom.getBidderCheck());
    }

    public void updateProductState(Product product, int state) throws IOException {
        if ( 0 > state || state > 3 ) throw new IllegalArgumentException("Illegal state");

        product.setState(state);

        productService.upsertProductsToElastic(new ElasticProduct(product));
        productRepository.save(product);

//        return new UpdateStateResponse(productId, res.getState());
    }
}
