package com.capstone.bidmarkit.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductDetailResponse {
    private List<String> url;
    private String name;
    private int bidPrice;
    private int initPrice;
    private int price;
    private LocalDateTime deadline;
    private String sellerName;
    private String content;

    public ProductDetailResponse(List<String> url, String name, int bidPrice, int initPrice, int price, LocalDateTime deadline, String sellerName, String content) {
        this.url = url;
        this.name = name;
        this.bidPrice = bidPrice;
        this.initPrice = initPrice;
        this.price = price;
        this.deadline = deadline;
        this.sellerName = sellerName;
        this.content = content;
    }
}
