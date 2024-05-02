package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.Member;
import com.capstone.bidmarkit.dto.CustomUserDetails;
import com.capstone.bidmarkit.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String id) {
        Optional<Member> memberData = memberRepository.findById(id);

        if (memberData.isPresent()) {
            return new CustomUserDetails(memberData.get());
        }

        return null;
    }
}
