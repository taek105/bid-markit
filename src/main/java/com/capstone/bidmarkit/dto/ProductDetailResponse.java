package com.capstone.bidmarkit.dto;

import com.capstone.bidmarkit.domain.Question;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailResponse {
    private List<String> images;
    private List<QnAResponse> questions;
    private String productName;
    private Integer category;
    private int bidPrice;
    private int initPrice;
    private int price;
    private int state;
    private LocalDateTime deadline;
    private String sellerName;
    private String content;
}
