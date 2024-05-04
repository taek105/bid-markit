package com.capstone.bidmarkit.repository;

import com.capstone.bidmarkit.domain.AutoBid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutoBidRepository extends JpaRepository<AutoBid, Integer> {
}
