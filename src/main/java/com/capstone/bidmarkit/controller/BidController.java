package com.capstone.bidmarkit.controller;

import com.capstone.bidmarkit.domain.Bid;
import com.capstone.bidmarkit.dto.AddAutoBidRequest;
import com.capstone.bidmarkit.dto.AddBidRequest;
import com.capstone.bidmarkit.dto.BidResponse;
import com.capstone.bidmarkit.service.AutoBidService;
import com.capstone.bidmarkit.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BidController {
    private final BidService bidService;
    private final AutoBidService autoBidService;

    @PostMapping("/bid")
    public ResponseEntity<Void> bid(@RequestHeader(name="Authorization") String token, @RequestBody AddBidRequest request) {
        bidService.save(token.substring(7), request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auto-bid")
    public ResponseEntity<Void> autoBid(@RequestHeader(name="Authorization") String token, @RequestBody AddAutoBidRequest request) {
        autoBidService.save(token.substring(7), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("bids/{productId}")
    public ResponseEntity<List<BidResponse>> getBidLog(@PathVariable int productId) {
        return ResponseEntity.ok().body(bidService.findAllByProductId(productId));
    }

}