package com.capstone.bidmarkit.controller;

import com.capstone.bidmarkit.dto.*;
import com.capstone.bidmarkit.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class ProductController {
    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<Void> enrollProduct(@RequestBody AddProductRequest request) {
        productService.save(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/products")
    public ResponseEntity<Page<ProductBriefResponse>> getProducts(@RequestBody ProductPageRequest request) {
        return ResponseEntity.ok().body(productService.findAllOrderByDeadlineAsc(PageRequest.of(request.getPageNum(), request.getSize())));
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDetailResponse> getDetail(@PathVariable int productId) {
        return ResponseEntity.ok(productService.findDetail(productId));
    }

    @PostMapping("/purchase")
    public ResponseEntity<Void> purchase(@RequestBody PurchaseRequest request) {
        productService.purchaseProduct(request.getProductId());
        return ResponseEntity.ok().build();
    }
}
