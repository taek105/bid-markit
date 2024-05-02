package com.capstone.bidmarkit.repository;

import com.capstone.bidmarkit.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> { }
