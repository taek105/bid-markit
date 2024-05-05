package com.capstone.bidmarkit.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name = "product_imgs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ProductImg {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "img_id", updatable = false)
    private Integer id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "is_thumbnail")
    private Boolean isThumbnail;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public ProductImg(int id, Product product, String imgUrl, Boolean isThumbnail) {
        this.id = id;
        this.product = product;
        this.imgUrl = imgUrl;
        this.isThumbnail = isThumbnail;
    }

    public void setProduct(Product product) {
        if(this.product != null) {
            this.product.getImages().remove(this);
        }
        this.product = product;
        product.getImages().add(this);
    }
}
