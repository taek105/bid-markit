package com.capstone.bidmarkit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AddProductRequest {
    private String productName;
    private Integer category;
    private String content;
    private int initPrice;
    private int price;
    private int deadline; // 일 수 기준
    private List<MultipartFile> images;
}
