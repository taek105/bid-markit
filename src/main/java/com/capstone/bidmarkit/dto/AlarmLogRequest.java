package com.capstone.bidmarkit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlarmLogRequest {
    private int type;
    private String message, memberId;
}
