package com.capstone.bidmarkit.repository;

import com.capstone.bidmarkit.domain.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Integer> {
    List<Bid> findAllByProductId(int productId);
}
