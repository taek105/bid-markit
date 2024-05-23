package com.capstone.bidmarkit.repository;

import com.capstone.bidmarkit.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> { }
