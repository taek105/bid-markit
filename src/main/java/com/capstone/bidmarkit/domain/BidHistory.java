package com.capstone.bidmarkit.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "bid_history")
public class BidHistory implements Serializable {
    @Id
    private String memberId;

    private List<String> keyword;
    private List<Integer> category;
}
