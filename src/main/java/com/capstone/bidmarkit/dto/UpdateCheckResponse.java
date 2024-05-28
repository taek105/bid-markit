package com.capstone.bidmarkit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCheckResponse {
    private Byte sellerCheck, bidderCheck;
}
