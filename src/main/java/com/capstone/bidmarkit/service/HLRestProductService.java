package com.capstone.bidmarkit.service;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.capstone.bidmarkit.dto.ElasticProduct;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.search.suggest.completion.FuzzyOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HLRestProductService {
    private final RestHighLevelClient highLevelClient;

//    public List<String> suggestKeywords(String keyword) throws IOException {
//        List<String> suggestions = new ArrayList<>();
//        SuggestBuilder suggestBuilder = new SuggestBuilder();
//        CompletionSuggestionBuilder completionSuggestionBuilder = new CompletionSuggestionBuilder("product_name");
//        completionSuggestionBuilder.prefix(keyword);
//        completionSuggestionBuilder.size(10);
//        suggestBuilder.addSuggestion("suggest_product_name", completionSuggestionBuilder);
//
//        SearchRequest searchRequest = new SearchRequest("products");
//        searchRequest.source(new SearchSourceBuilder().suggest(suggestBuilder));
//
//        SearchResponse searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//        Suggest suggest = searchResponse.getSuggest();
//        CompletionSuggestion completionSuggestion = suggest.getSuggestion("suggest_product_name");
//
//        for (CompletionSuggestion.Entry entry : completionSuggestion.getEntries()) {
//            for (CompletionSuggestion.Entry.Option option : entry) {
//                String suggestText = option.getText().string();
//                suggestions.add(suggestText);
//            }
//        }
//
//        return suggestions;
//    }

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

    public Page<ElasticProduct> findAllByKeyword(String keywords, Pageable pageable) throws IOException {
        SearchRequest searchRequest = new SearchRequest("products");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("product_name", keywords).boost(2.0f))
            .from((int) pageable.getOffset())
            .size(pageable.getPageSize());
        searchRequest.source(sourceBuilder);

        SearchResponse response = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        List<ElasticProduct> products = new ArrayList<>();

        response.getHits().forEach(hit -> {
            ElasticProduct found = ElasticProduct.builder()
                    .productId(Integer.parseInt(hit.getSourceAsMap().get("product_id").toString()))
                    .productName(hit.getSourceAsMap().get("product_name").toString())
                    .content(hit.getSourceAsMap().get("content").toString())
                    .thumbnail(hit.getSourceAsMap().get("thumbnail").toString())
                    .category(Integer.parseInt(hit.getSourceAsMap().get("category").toString()))
                    .price(Integer.parseInt(hit.getSourceAsMap().get("price").toString()))
                    .bidPrice(Integer.parseInt(hit.getSourceAsMap().get("bid_price").toString()))
                    .state(Integer.parseInt(hit.getSourceAsMap().get("state").toString()))
                    .deadline(hit.getSourceAsMap().get("deadline").toString())
                    .build();

            products.add(found);
        });

        return new PageImpl<>(products, pageable, response.getHits().getTotalHits().value);
    }
}