package com.capstone.bidmarkit.service;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HLRestProductService {
    private final RestHighLevelClient highLevelClient;

    public List<String> suggestKeywords(String keyword) throws IOException {
        List<String> suggestions = new ArrayList<>();
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        CompletionSuggestionBuilder completionSuggestionBuilder = new CompletionSuggestionBuilder("product_name");
        completionSuggestionBuilder.prefix(keyword);
        completionSuggestionBuilder.size(10);
        suggestBuilder.addSuggestion("suggest_product_name", completionSuggestionBuilder);

        SearchRequest searchRequest = new SearchRequest("products");
        searchRequest.source(new SearchSourceBuilder().suggest(suggestBuilder));

        SearchResponse searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        Suggest suggest = searchResponse.getSuggest();
        CompletionSuggestion completionSuggestion = suggest.getSuggestion("suggest_product_name");

        for (CompletionSuggestion.Entry entry : completionSuggestion.getEntries()) {
            for (CompletionSuggestion.Entry.Option option : entry) {
                String suggestText = option.getText().string();
                suggestions.add(suggestText);
            }
        }

        return suggestions;
    }
}
