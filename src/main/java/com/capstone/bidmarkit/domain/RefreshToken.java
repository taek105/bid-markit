package com.capstone.bidmarkit.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "member_id", nullable = false, unique = true)
    private String memberId;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Builder
    public RefreshToken(String memberId, String refreshToken) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }

    public RefreshToken update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;

        return this;
    }
}