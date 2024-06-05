package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.*;

import com.capstone.bidmarkit.dto.*;
import com.capstone.bidmarkit.repository.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class AlarmLogService {

    private final AlarmLogRepository alarmLogRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public void save(AlarmLogRequest request) {
        alarmLogRepository.save(AlarmLog.builder()
                .memberId(request.getMemberId())
                .type(request.getType())
                .message(request.getMessage())
                .build());
    }

    public List<AlarmLogResponse> getAlarmLogsByMemberId(String memberId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QAlarmLog alarmLog = QAlarmLog.alarmLog;

        List<AlarmLogResponse> results = queryFactory
                .select(Projections.constructor(AlarmLogResponse.class, alarmLog.type, alarmLog.message, alarmLog.createdAt))
                .from(alarmLog)
                .where(alarmLog.memberId.eq(memberId))
                .orderBy(alarmLog.createdAt.desc())
//                .limit(100)
                .fetch();

        return results;
    }

}
