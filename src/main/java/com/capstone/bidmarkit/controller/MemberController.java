package com.capstone.bidmarkit.controller;

import com.capstone.bidmarkit.dto.AddMemberRequest;
import com.capstone.bidmarkit.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member")
    public ResponseEntity<Void> signup(@RequestBody AddMemberRequest request) {
        memberService.save(request);
        return ResponseEntity.ok().build();
    }
}
