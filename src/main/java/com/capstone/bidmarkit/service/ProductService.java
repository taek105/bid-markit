package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.Product;
import com.capstone.bidmarkit.domain.ProductImg;
import com.capstone.bidmarkit.domain.QProduct;
import com.capstone.bidmarkit.domain.QProductImg;
import com.capstone.bidmarkit.dto.AddProductRequest;
import com.capstone.bidmarkit.dto.ProductBriefResponse;
import com.capstone.bidmarkit.dto.ProductDetailResponse;
import com.capstone.bidmarkit.repository.ProductImgRepository;
import com.capstone.bidmarkit.repository.ProductRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;

    @PersistenceContext
    private EntityManager entityManager;


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

    public Page<ProductBriefResponse> findAllOrderByDeadlineAsc(Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QProduct product = QProduct.product;
        QProductImg productImg = QProductImg.productImg;

        List<ProductBriefResponse> results = queryFactory
                .select(Projections.constructor(ProductBriefResponse.class, productImg.imgUrl, product.name, product.bidPrice, product.price, product.deadline))
                .from(product)
                .leftJoin(product.images, productImg)
                .groupBy(product.id, productImg.imgUrl)
                .where(productImg.isThumbnail.isTrue())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(results, pageable, queryFactory.select(product.count()).from(product)::fetchCount);
    }

    public ProductDetailResponse findDetail(int productId) {
        ProductDetailResponse res = new ProductDetailResponse();

        Product a = productRepository.findDetailById(productId);

        res.setImages(productImgRepository.findByProductId(productId)
                .stream()
                .map(ProductImg::getImgUrl)
                .collect(Collectors.toList()));
        res.setProductName(a.getName());
        res.setBidPrice(a.getBidPrice());
        res.setInitPrice(a.getInitPrice());
        res.setPrice(a.getPrice());
        res.setDeadline(a.getDeadline());
        res.setSellerName(a.getMemberId());
        res.setContent(a.getContent());

        return res;
    }

    public void purchaseProduct(int productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.setState((byte) 3);
        productRepository.save(product);
    }
}
