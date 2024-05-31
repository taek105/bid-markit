package com.capstone.bidmarkit.dto;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.capstone.bidmarkit.domain.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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

    public ElasticProduct(Product product) {
        product_id = product.getId();
        product_name = product.getName();
        category = product.getCategory();
        state = product.getState();
        content = product.getContent();
        thumbnail = product.getImages().get(0).getImgUrl();
        bid_price = product.getBidPrice();
        price = product.getPrice();

        Instant instant = product.getDeadline().atZone(ZoneId.of("UTC")).toInstant();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        deadline = formatter.format(instant);
    }
}
