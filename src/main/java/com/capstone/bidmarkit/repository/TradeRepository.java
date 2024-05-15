package com.capstone.bidmarkit.repository;

import com.capstone.bidmarkit.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Integer> {
}
