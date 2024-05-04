package com.capstone.bidmarkit.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Product {
    @Id
    @Column(name = "product_id", updatable = false)
    private int id;

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "product_name")
    private String name;

    @Column(name = "category")
    private int category;

    @Column(name = "state")
    private byte state;

    @Column(name = "content")
    private String content;

    @Column(name = "init_price")
    private int initPrice;

    @Column(name = "bid_price")
    private int bidPrice;

    @Column(name = "price")
    private int price;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Product(int id, String memberId, String name, int category, String content, int initPrice, int price, LocalDateTime deadline) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.category = category;
        this.content = content;
        this.initPrice = initPrice;
        this.price = price;
        this.deadline = deadline;

        this.state = 0;
        this.bidPrice = initPrice;
    }
}
