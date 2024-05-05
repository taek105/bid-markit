package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.Product;
import com.capstone.bidmarkit.domain.ProductImg;
import com.capstone.bidmarkit.dto.AddProductRequest;
import com.capstone.bidmarkit.dto.ProductDetailResponse;
import com.capstone.bidmarkit.dto.ProductListResponse;
import com.capstone.bidmarkit.repository.ProductImgRepository;
import com.capstone.bidmarkit.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;

    public int save(AddProductRequest dto) {
        productImgRepository.saveAll(dto.getImages());
        return productRepository.save(
                Product.builder()
                        .id(dto.getId())
                        .memberId(dto.getMemberId())
                        .name(dto.getName())
                        .category(dto.getCategory())
                        .content(dto.getContent())
                        .initPrice(dto.getInitPrice())
                        .price(dto.getPrice())
                        .deadline(dto.getDeadline())
                        .build()
        ).getId();
    }
    public ProductDetailResponse findDetail(int productId) {
        ProductDetailResponse res = new ProductDetailResponse();

        Product a = productRepository.findDetailById(productId);

        res.setUrl(productImgRepository.findByProductId(productId)
                .stream()
                .map(ProductImg::getImgUrl)
                .collect(Collectors.toList()));
        res.setName(a.getName());
        res.setBidPrice(a.getBidPrice());
        res.setInitPrice(a.getInitPrice());
        res.setPrice(a.getPrice());
        res.setDeadline(a.getDeadline());
        res.setSellerName(a.getMemberId());
        res.setContent(a.getContent());

        return res;
    }
    public List<ProductListResponse> findPersonalizedList() {

        // 개인화 로직 추가해야 함

        int[] PersonalizedResult = {1, 2};

        List<Product> products = productRepository.findProductsById(PersonalizedResult);
        if (products.isEmpty()) {
            throw new IllegalArgumentException("ersonalized list load error");
        }

        List<ProductListResponse> res = null;

        return res;
    }

    public void purchaseProduct(int productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.setState((byte) 3);
        productRepository.save(product);
    }
}
