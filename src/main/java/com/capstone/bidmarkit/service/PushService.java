package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.dto.PushAlarmRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class PushService {

    private final RestTemplate restTemplate;

    @Value("${push.server.url}")
    private String apiUrl; // 알림 서버의 URL


    public void pushAlarm (PushAlarmRequest request) {
        try {
            restTemplate.postForObject(apiUrl + "/push/" + request.getMemberId(), request, Void.class);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("error in pushalarm");
        }
    }
}
