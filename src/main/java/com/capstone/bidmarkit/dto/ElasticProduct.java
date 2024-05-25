package com.capstone.bidmarkit.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElasticProduct {
    private Integer productId;
    private String productName;
    private Integer category;
    private Integer state;
    private String content;
    private String thumbnail;
    private Integer bidPrice;
    private Integer price;
    private String deadline;
}
