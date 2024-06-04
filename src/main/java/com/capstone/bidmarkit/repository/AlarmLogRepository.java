package com.capstone.bidmarkit.repository;

import com.capstone.bidmarkit.domain.AlarmLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmLogRepository extends JpaRepository<AlarmLog, Integer> {
    List<AlarmLog> findAllByMemberId(String memberId);
}
