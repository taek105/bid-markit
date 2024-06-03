package com.capstone.bidmarkit.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id", updatable = false)
    private Integer id;

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "product_name")
    private String name;

    @Column(name = "category")
    private Integer category;

    @Column(name = "state")
    private Integer state;

    @Column(name = "content")
    private String content;

    @Column(name = "init_price")
    private Integer initPrice;

    @Column(name = "bid_price")
    private Integer bidPrice;

    @Column(name = "price")
    private Integer price;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImg> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Product(int id, String memberId, String name, Integer category, String content, int initPrice, int price, LocalDateTime deadline) {
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
