package com.capstone.bidmarkit.dto;

import com.capstone.bidmarkit.domain.ProductImg;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AddProductRequest {
    private int id;
    private String memberId;
    private String name;
    private String category;
    private String content;
    private int initPrice;
    private int price;
    private LocalDateTime deadline;
    private List<ProductImg> images;
}
