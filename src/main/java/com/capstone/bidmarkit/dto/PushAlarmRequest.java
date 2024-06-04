package com.capstone.bidmarkit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PushAlarmRequest {
    private String memberId;
    private String productName;
    private String imgURL;
    private String content;
    private int type;
}