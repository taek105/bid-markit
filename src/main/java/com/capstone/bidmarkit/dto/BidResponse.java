package com.capstone.bidmarkit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BidResponse {
    private String memberId;
    private Integer price;
    private LocalDateTime createdAt;
}
