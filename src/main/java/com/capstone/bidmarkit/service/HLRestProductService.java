package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.dto.ProductBriefResponse;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class HLRestProductService {
    private final RestHighLevelClient highLevelClient;

    public List<String> suggestKeywords(String productName) throws IOException {
        SearchRequest searchRequest = new SearchRequest("products");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("product_name", productName));
        searchSourceBuilder.size(10);
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        List<String> result = new ArrayList<>(10);
        response.getHits().forEach(hit -> {
            String productNameFromHit = hit.getSourceAsMap().get("product_name").toString();
            result.add(productNameFromHit);
        });

        return result;
    }

    public Page<ProductBriefResponse> findAll(
            String keywords, Integer category, Integer state, Integer sort, Pageable pageable
    ) throws IOException {
        SearchRequest searchRequest = new SearchRequest("products");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilders = QueryBuilders.boolQuery();
        if(!keywords.isEmpty()) queryBuilders.must(QueryBuilders.matchQuery("product_name", keywords));
        if(0 <= category && category <= 7) queryBuilders.must(QueryBuilders.matchQuery("category", category));
        if(0 <= state && state <= 3) queryBuilders = queryBuilders.must(QueryBuilders.matchQuery("state", state));

        sourceBuilder.query(queryBuilders).from((int) pageable.getOffset()).size(pageable.getPageSize());
        switch (sort) {
            case 0: sourceBuilder.sort("updated_at", SortOrder.DESC); break;
            case 1: sourceBuilder.sort("deadline", SortOrder.ASC); break;
            case 2: sourceBuilder.sort("bid_price", SortOrder.ASC); break;
            case 3: sourceBuilder.sort("bid_price", SortOrder.DESC); break;
        }

        searchRequest.source(sourceBuilder);

        SearchResponse response = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        List<ProductBriefResponse> products = new ArrayList<>();

        response.getHits().forEach(hit -> {
            Map<String, Object> map = hit.getSourceAsMap();
            ProductBriefResponse found = ProductBriefResponse.builder()
                    .productId(Integer.parseInt(map.get("product_id").toString()))
                    .productName(map.get("product_name").toString())
                    .thumbnail(map.get("thumbnail").toString())
                    .category(Integer.parseInt(map.get("category").toString()))
                    .price(Integer.parseInt(map.get("price").toString()))
                    .bidPrice(Integer.parseInt(map.get("bid_price").toString()))
                    .state(Integer.parseInt(map.get("state").toString()))
                    .deadline(Instant.parse((String) map.get("deadline")).atZone(ZoneId.of("UTC")).toLocalDateTime())
                    .build();

            products.add(found);
        });

        return new PageImpl<>(products, pageable, response.getHits().getTotalHits().value);
    }
}