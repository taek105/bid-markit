package com.capstone.bidmarkit.repository;

import com.capstone.bidmarkit.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findDetailById(int productId);

    List<Product> findProductsById(int[] personalizedList);
}
