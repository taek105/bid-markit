package com.capstone.bidmarkit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class QnAResponse {
    private Integer questionId;
    private String memberId;
    private String content;
    private LocalDateTime createdAt;
    private String ansContent;
    private LocalDateTime ansCreatedAt;
}
