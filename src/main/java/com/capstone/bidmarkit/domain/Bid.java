package com.capstone.bidmarkit.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name = "bids")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "bid_id", updatable = false)
    private Integer id;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "price")
    private Integer price;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Bid(int id, int productId, String memberId, int price) {
        this.id = id;
        this.productId = productId;
        this.memberId = memberId;
        this.price = price;
    }
}
