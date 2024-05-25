package com.capstone.bidmarkit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ElasticProduct {
    private Integer productId;
    private String productName;
    private Integer category;
    private Integer state;
    private String content;
    private String thumbnail;
    private Integer bidPrice;
    private Integer price;
    private LocalDateTime deadline;
}
