package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.dto.ProductBriefResponse;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public Page<ProductBriefResponse> findAllByKeyword(String keywords, Pageable pageable) throws IOException {
        SearchRequest searchRequest = new SearchRequest("products");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("product_name", keywords).boost(2.0f))
                .from((int) pageable.getOffset())
                .size(pageable.getPageSize());
        searchRequest.source(sourceBuilder);

        SearchResponse response = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        List<ProductBriefResponse> products = new ArrayList<>();

        response.getHits().forEach(hit -> {
            ProductBriefResponse found = ProductBriefResponse.builder()
                    .productId(Integer.parseInt(hit.getSourceAsMap().get("product_id").toString()))
                    .productName(hit.getSourceAsMap().get("product_name").toString())
                    .thumbnail(hit.getSourceAsMap().get("thumbnail").toString())
                    .category(Integer.parseInt(hit.getSourceAsMap().get("category").toString()))
                    .price(Integer.parseInt(hit.getSourceAsMap().get("price").toString()))
                    .bidPrice(Integer.parseInt(hit.getSourceAsMap().get("bid_price").toString()))
                    .state(Integer.parseInt(hit.getSourceAsMap().get("state").toString()))
                    .deadline(LocalDateTime.parse(hit.getSourceAsMap().get("deadline").toString()))
                    .build();

            products.add(found);
        });

        return new PageImpl<>(products, pageable, response.getHits().getTotalHits().value);
    }
}