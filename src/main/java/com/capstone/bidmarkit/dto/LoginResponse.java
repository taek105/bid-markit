package com.capstone.bidmarkit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponse {
    String nickname, refreshToken, accessToken;
}
