package com.capstone.bidmarkit.controller;

import com.capstone.bidmarkit.dto.*;
import com.capstone.bidmarkit.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class ProductController {
    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<Void> enrollProduct(@RequestBody AddProductRequest request) {
        productService.save(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/products/{pageNum}/{size}")
    public ResponseEntity<Page<ProductBriefResponse>> getProducts(@PathVariable("pageNum") int pageNum, @PathVariable("size") int size) {
        return ResponseEntity.ok().body(productService.findAllOrderByDeadlineAsc(PageRequest.of(pageNum, size)));
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDetailResponse> getDetail(@PathVariable int productId) {
        return ResponseEntity.ok().body(productService.findDetail(productId));
    }

    @PostMapping("/purchase")
    public ResponseEntity<Void> purchase(@RequestHeader(name="Authorization") String token, @RequestBody PurchaseRequest request) {
        productService.purchaseProduct(token.substring(7), request.getProductId());
        return ResponseEntity.ok().build();
    }
}
