package com.capstone.bidmarkit.controller;

import com.capstone.bidmarkit.dto.AlarmLogResponse;
import com.capstone.bidmarkit.service.AlarmLogService;
import com.capstone.bidmarkit.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AlarmLogController {
    private final AlarmLogService alarmLogService;
    private final TokenService tokenService;

    @GetMapping("/alarmlog")
    public ResponseEntity<List<AlarmLogResponse>> getAlarmLog(@RequestHeader(name="Authorization") String token) {
        return ResponseEntity.ok().body(alarmLogService.getAlarmLogsByMemberId(tokenService.getMemberId(token.substring(7))));
    }
}