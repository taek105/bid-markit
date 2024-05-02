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

@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id
    @Column(name = "member_id", updatable = false)
    private String id;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "cancel_purchase")
    private Integer cancelPurchase;

    @Column(name = "cancel_sale")
    private Integer cancelSale;

    @Column(name = "role")
    private String role;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Member(String id, String password, String name, String nickname, String role) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.role = role;
        cancelPurchase = 0;
        cancelSale = 0;
    }
}
