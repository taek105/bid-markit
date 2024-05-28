package com.capstone.bidmarkit.dto;

import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@AllArgsConstructor
@Builder
public class ProductBriefResponse {
    String thumbnail, productName;
    Integer category, productId, bidPrice, price, state;
    LocalDateTime deadline;

    public static ProductBriefResponse from(Hit<ElasticProduct> elasticProductHit) {
        ElasticProduct hit = elasticProductHit.source();
        return ProductBriefResponse.builder()
                .productId(hit.getProduct_id())
                .productName(hit.getProduct_name())
                .category(hit.getCategory())
                .thumbnail(hit.getThumbnail())
                .bidPrice(hit.getBid_price())
                .price(hit.getPrice())
                .state(hit.getState())
                .deadline(Instant.parse(hit.getDeadline()).atZone(ZoneId.of("UTC")).toLocalDateTime())
                .build();
    }
}
