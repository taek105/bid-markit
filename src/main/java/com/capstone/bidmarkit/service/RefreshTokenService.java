package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.RefreshToken;
import com.capstone.bidmarkit.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }

    public RefreshToken save(String memberId, String token) {
        Optional<RefreshToken> found = refreshTokenRepository.findByMemberId(memberId);
        if(found.isPresent()) {
            RefreshToken refreshToken = found.get();
            refreshToken.update(token);
            refreshTokenRepository.save(refreshToken);
            return refreshToken;
        }
        return refreshTokenRepository.save(RefreshToken.builder().memberId(memberId).refreshToken(token).build());
    }
}