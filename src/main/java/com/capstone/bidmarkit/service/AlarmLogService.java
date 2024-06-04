package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.*;

import com.capstone.bidmarkit.dto.*;
import com.capstone.bidmarkit.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class AlarmLogService {

    private final AlarmLogRepository alarmLogRepository;

    public void save(AlarmLogRequest request) {
        alarmLogRepository.save(AlarmLog.builder()
                .memberId(request.getMemberId())
                .type(request.getType())
                .message(request.getMessage())
                .build());
    }

    public List<AlarmLogResponse> getAlarmLogsByMemberId(String memberId) {
        List<AlarmLog> alarmLogs = alarmLogRepository.findAllByMemberId(memberId);
        return alarmLogs.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private AlarmLogResponse convertToResponse(AlarmLog alarmLog) {
        return new AlarmLogResponse(
                alarmLog.getType(),
                alarmLog.getMessage(),
                alarmLog.getCreatedAt()
        );
    }
}
