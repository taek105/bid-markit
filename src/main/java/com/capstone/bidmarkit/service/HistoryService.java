package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.BidHistory;
import com.capstone.bidmarkit.domain.SearchHistory;
import com.capstone.bidmarkit.repository.BidHistoryRepository;
import com.capstone.bidmarkit.repository.SearchHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HistoryService {
    private final SearchHistoryRepository searchHistoryRepository;
    private final BidHistoryRepository bidHistoryRepository;
    private final int HISTORY_SIZE = 5;

    public void upsertSearchHistory(String memberId, String keyword, int category) {
        Optional<SearchHistory> found = searchHistoryRepository.findById(memberId);
        SearchHistory newSearchHistory = SearchHistory.builder()
                .memberId(memberId)
                .keyword(new ArrayList<>())
                .category(new ArrayList<>())
                .build();
        newSearchHistory.getKeyword().add(keyword);
        newSearchHistory.getCategory().add(category);
        if (found.isPresent()) {
            int toIndex = Integer.min(found.get().getKeyword().size(), HISTORY_SIZE - 1);
            newSearchHistory.getKeyword().addAll(found.get().getKeyword().subList(0, toIndex));
            newSearchHistory.getCategory().addAll(found.get().getCategory().subList(0, toIndex));
        }

        searchHistoryRepository.save(newSearchHistory);
    }

    public void upsertBidHistory(String memberId, String keyword, int category) {
        Optional<BidHistory> found = bidHistoryRepository.findById(memberId);
        BidHistory newBidHistory = BidHistory.builder()
                .memberId(memberId)
                .keyword(new ArrayList<>())
                .category(new ArrayList<>())
                .build();
        newBidHistory.getKeyword().add(keyword);
        newBidHistory.getCategory().add(category);
        if (found.isPresent()) {
            int toIndex = Integer.min(found.get().getKeyword().size(), HISTORY_SIZE - 1);
            newBidHistory.getKeyword().addAll(found.get().getKeyword().subList(0, toIndex));
            newBidHistory.getCategory().addAll(found.get().getCategory().subList(0, toIndex));
        }

        bidHistoryRepository.save(newBidHistory);
    }

    public void showSearchHistory(String memberId) {
        Optional<SearchHistory> found = searchHistoryRepository.findById(memberId);
        if(found.isPresent()) {
            List<String> keywords = found.get().getKeyword();
            List<Integer> categories = found.get().getCategory();

            StringBuilder sb = new StringBuilder();
            sb.append("keywords: ");
            for (String keyword: keywords) sb.append(keyword).append(" ");
            sb.append("\ncategories: ");
            for (int category: categories) sb.append(category).append(" ");
            System.out.println(sb);
        }
    }
}
