package com.capstone.bidmarkit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AlarmLogResponse {
    private int type;
    private String content;
    private LocalDateTime created_at;
}