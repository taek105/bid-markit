package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.dto.PushAlarmRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Service
public class PushService {

    private final RestTemplate restTemplate;
    private final WebClient webClient;

    @Value("${push.server.url}")
    private String apiUrl; // 알림 서버의 URL


    public void pushAlarm (PushAlarmRequest request) {
        try {
            restTemplate.postForObject(apiUrl + "/push/" + request.getMemberId(), request, Void.class);
//            webClient.get().uri(uriBuilder -> uriBuilder
//                    .path("/push/" + request.getMemberId())
//                    .queryParam("productName", request.getProductName())
//                    .queryParam("imgURL", request.getImgURL())
//                    .queryParam("content", request.getContent())
//                    .queryParam("type", request.getType())
//                    .build()
//            ).retrieve().bodyToMono(Void.class);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("error in pushalarm");
        }
    }
}
