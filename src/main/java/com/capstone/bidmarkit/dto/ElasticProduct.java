package com.capstone.bidmarkit.dto;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown=true)
public class ElasticProduct implements Serializable {
    private Integer product_id;
    private String product_name;
    private Integer category;
    private Integer state;
    private String content;
    private String thumbnail;
    private Integer bid_price;
    private Integer price;
    private String deadline;
}
