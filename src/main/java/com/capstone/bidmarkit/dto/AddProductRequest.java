package com.capstone.bidmarkit.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AddProductRequest {
    private int id;
    private String memberId;
    private String name;
    private int category;
    private String content;
    private int initPrice;
    private int price;
    private LocalDateTime deadline;
}
