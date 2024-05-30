package com.capstone.bidmarkit.repository;

import com.capstone.bidmarkit.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface MemberRepository extends JpaRepository<Member, String> {

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.cancelSale = m.cancelSale + 1 WHERE m.id = :memberId")
    void incrCancelSale(@Param("memberId") String memberId);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.cancelPurchase = m.cancelPurchase + 1 WHERE m.id = :memberId")
    void incrCancelPurchase(@Param("memberId") String memberId);
}
