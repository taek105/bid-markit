package com.capstone.bidmarkit.controller;

import com.capstone.bidmarkit.dto.*;
import com.capstone.bidmarkit.service.HLRestProductService;
import com.capstone.bidmarkit.service.ProductService;
import com.capstone.bidmarkit.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ProductController {
    private final ProductService productService;
    private final HLRestProductService hlRestProductService;
    private final TokenService tokenService;

    @PostMapping("/products")
    public ResponseEntity<AddProductResponse> enrollProduct(@RequestHeader(name="Authorization") String token, AddProductRequest request) throws IOException {
        return ResponseEntity.ok().body(productService.save(tokenService.getMemberId(token.substring(7)), request));
    }

    @GetMapping("/products")
    public ResponseEntity<Page<ProductBriefResponse>> getProducts(@RequestParam int pageNum, @RequestParam int size) {
        return ResponseEntity.ok().body(productService.findAllOrderByDeadlineAsc(PageRequest.of(pageNum, size)));
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDetailResponse> getDetail(
            @RequestHeader(name="Authorization", required = false) String token, @PathVariable int productId) {
        String memberId = token == null ? "" : tokenService.getMemberId(token.substring(7));
        return ResponseEntity.ok().body(productService.findDetail(memberId, productId));
    }

    @PostMapping("/purchase")
    public ResponseEntity<Void> purchase(@RequestHeader(name="Authorization") String token, @RequestBody PurchaseRequest request) {
        productService.purchaseProduct(tokenService.getMemberId(token.substring(7)), request.getProductId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/purchase")
    public ResponseEntity<Page<GetPurchaseResponse>> getPurchase(
            @RequestHeader(name="Authorization") String token, @RequestParam int pageNum, @RequestParam int size) {
        return ResponseEntity.ok().body(productService.findAllPurchased(tokenService.getMemberId(token.substring(7)), PageRequest.of(pageNum, size)));
    }

    @GetMapping("/sale")
    public ResponseEntity<Page<ProductBriefResponse>> getSale(
            @RequestHeader(name="Authorization") String token, @RequestParam(required = false) Integer state, @RequestParam int pageNum, @RequestParam int size
    ) {
        return ResponseEntity.ok().body(productService.findAllSale(tokenService.getMemberId(token.substring(7)), state == null ? 4 : state.intValue(), PageRequest.of(pageNum, size)));
    }

    @GetMapping("/suggest/keywords")
    public ResponseEntity<List<String>> suggestKeywords(@RequestParam String keyword) throws IOException {
        return ResponseEntity.ok().body(hlRestProductService.suggestKeywords(keyword));
    }

    @GetMapping("/search/products")
    public ResponseEntity<Page<ProductBriefResponse>> search(
            @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "-1") Integer category,
            @RequestParam(defaultValue = "0") Integer state, @RequestParam(defaultValue = "0") Integer sort,
            @RequestParam int pageNum, @RequestParam int size
    ) throws IOException {
        return ResponseEntity.ok().body(
                hlRestProductService.findAll(keyword, category, state, sort, PageRequest.of(pageNum, size))
        );
    }

    @GetMapping("/suggest/products")
    public ResponseEntity<Page<ProductBriefResponse>> suggestProducts(
            @RequestHeader(name="Authorization") String token, @RequestParam int pageNum, @RequestParam int size
    ) throws IOException {
        return ResponseEntity.ok().body(productService.suggestProducts(tokenService.getMemberId(token.substring(7)), PageRequest.of(pageNum, size)));
    }
}
