package com.capstone.bidmarkit.controller;

import com.capstone.bidmarkit.domain.Product;
import com.capstone.bidmarkit.dto.AddProductRequest;
import com.capstone.bidmarkit.dto.ProductDetailResponse;
import com.capstone.bidmarkit.dto.ProductListResponse;
import com.capstone.bidmarkit.dto.PurchaseRequest;
import com.capstone.bidmarkit.service.ProductService;
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
public class ProductController {
    private final ProductService productService;

    @PostMapping("/product")
    public ResponseEntity<Void> enrollProduct(@RequestBody AddProductRequest request) {
        productService.save(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductListResponse>> getProducts() {
        return ResponseEntity.ok(productService.findPersonalizedList());
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
