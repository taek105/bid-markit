package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.Member;
import com.capstone.bidmarkit.dto.AddMemberRequest;
import com.capstone.bidmarkit.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Member save(AddMemberRequest dto) {
        return memberRepository.save(
                Member.builder()
                        .id(dto.getId())
                        .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                        .name(dto.getName())
                        .nickname(dto.getNickname())
                        .role("ROLE_MEMBER")
                        .build()
        );
    }

    public Member findById(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected member"));
    }
}
