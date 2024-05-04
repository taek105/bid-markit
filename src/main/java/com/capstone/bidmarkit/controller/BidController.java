package com.capstone.bidmarkit.controller;

import com.capstone.bidmarkit.domain.Bid;
import com.capstone.bidmarkit.dto.AddAutoBidRequest;
import com.capstone.bidmarkit.dto.AddBidRequest;
import com.capstone.bidmarkit.service.AutoBidService;
import com.capstone.bidmarkit.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BidController {
    private final BidService bidService;
    private final AutoBidService autoBidService;

    @PostMapping("/bid")
    public ResponseEntity<Void> bid(@RequestBody AddBidRequest request) {
        bidService.save(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auto-bid")
    public ResponseEntity<Void> autoBid(@RequestBody AddAutoBidRequest request) {
        autoBidService.save(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("bids/{productId}")
    public ResponseEntity<List<Bid>> getBidLog(@PathVariable int productId) {
        return ResponseEntity.ok(bidService.findAllByProductId(productId));
    }

}
