package com.capstone.bidmarkit.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name = "product_imgs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ProductImg {
    @Id
    @Column(name = "img_id", updatable = false)
    private int id;

    @Column(name = "product_id")
    private int productId;

    @Column(name = "img_url")
    private String imgUrl;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public ProductImg(int id, int productId, String imgUrl) {
        this.id = id;
        this.productId = productId;
        this.imgUrl = imgUrl;
    }
}
