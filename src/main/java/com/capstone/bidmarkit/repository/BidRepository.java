package com.capstone.bidmarkit.repository;

import com.capstone.bidmarkit.domain.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Integer> {
    List<Bid> findAllByProductId(int productId);

    @Query("SELECT b FROM Bid b WHERE b.price = (SELECT MAX(b2.price) FROM Bid b2 WHERE b2.productId = :productId) AND b.productId = :productId")
    Optional<Bid> findTopByProductIdOrderByPriceDesc(@Param("productId") int productId);
}
