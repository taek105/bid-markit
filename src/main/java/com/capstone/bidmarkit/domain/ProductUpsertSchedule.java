package com.capstone.bidmarkit.domain;

import com.capstone.bidmarkit.dto.ElasticProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@RedisHash("scheduler")
public class ProductUpsertSchedule implements Serializable {
    @Id
    private String id;
    private List<ElasticProduct> productList;

    public ProductUpsertSchedule() {
        this.id = "product_upsert_schedule";
        this.productList = new ArrayList<>();
    }
}
