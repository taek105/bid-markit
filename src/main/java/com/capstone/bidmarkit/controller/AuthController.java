package com.capstone.bidmarkit.controller;

import com.capstone.bidmarkit.dto.CreateAccessTokenRequest;
import com.capstone.bidmarkit.dto.CreateAccessTokenResponse;
import com.capstone.bidmarkit.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final TokenService tokenService;

    @PostMapping("/accessToken")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse("Bearer " + newAccessToken));
    }

    @RequestMapping("/")
    public ResponseEntity<Void> test() {
        return ResponseEntity.ok().build();
    }
}
