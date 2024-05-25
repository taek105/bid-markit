package com.capstone.bidmarkit.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Table(name = "Chat_rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ChatRoom implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "chat_room_id", updatable = false)
    private Integer id;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "seller_id")
    private String sellerId;

    @Column(name = "bidder_id")
    private String bidderId;

    @Column(name = "seller_check")
    private Byte sellerCheck;

    @Column(name = "bidder_check")
    private Byte bidderCheck;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @CreatedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public ChatRoom(Integer productId, String sellerId, String bidderId) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.bidderId = bidderId;
        this.sellerCheck = 0;
        this.bidderCheck = 0;
    }
}

