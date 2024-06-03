package com.capstone.bidmarkit.controller;

import com.capstone.bidmarkit.dto.AddAnswerRequest;
import com.capstone.bidmarkit.dto.AddQuestionRequest;
import com.capstone.bidmarkit.service.QnAService;
import com.capstone.bidmarkit.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RequiredArgsConstructor
@Controller
public class QnAController {
    private final QnAService qnAService;
    private final TokenService tokenService;

    @PostMapping("/question")
    public ResponseEntity<Void> question(@RequestHeader(name="Authorization") String token,
                                         @RequestBody AddQuestionRequest request) {
        qnAService.saveQuestion(tokenService.getMemberId(token.substring(7)), request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/answer")
    public ResponseEntity<Void> answer(@RequestHeader(name="Authorization") String token,
                                       @RequestBody AddAnswerRequest request) throws IllegalAccessException {
        qnAService.saveAnswer(tokenService.getMemberId(token.substring(7)), request);
        return ResponseEntity.ok().build();
    }
}
