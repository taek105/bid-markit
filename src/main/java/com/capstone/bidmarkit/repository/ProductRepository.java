package com.capstone.bidmarkit.repository;

import com.capstone.bidmarkit.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p.name FROM Product p WHERE p.id = :productId")
    String findProductNameById(@Param("productId") int productId);

    @Query("SELECT p.price FROM Product p WHERE p.id = :productId")
    int findPriceById(@Param("productId") int productId);

    List<Product> findAllByDeadlineBeforeAndState(LocalDateTime deadline, Integer state);
}
