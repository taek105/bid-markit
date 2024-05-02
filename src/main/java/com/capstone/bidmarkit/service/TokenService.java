package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.config.jwt.TokenProvider;
import com.capstone.bidmarkit.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validToken(refreshToken) && refreshTokenService.findByRefreshToken(refreshToken) == null) {
            throw new IllegalArgumentException("Unexpected token");
        }

        String memberId = refreshTokenService.findByRefreshToken(refreshToken).getMemberId();
        Member member = memberService.findById(memberId);

        return tokenProvider.generateToken(member, Duration.ofHours(1));
    }
}
