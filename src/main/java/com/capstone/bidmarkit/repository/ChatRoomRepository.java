package com.capstone.bidmarkit.repository;

import com.capstone.bidmarkit.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findById(int roomId);

    @Query("SELECT cr.productId FROM ChatRoom cr WHERE cr.id = :roomId")
    int findProductIdByRoomId(@Param("roomId") int productId);

    @Modifying
    @Transactional
    @Query("UPDATE ChatRoom c SET c.updatedAt = :updatedAt WHERE c.id = :chatRoomId")
    void updateUpdatedAt(@Param("chatRoomId") int chatRoomId, @Param("updatedAt") LocalDateTime updatedAt);


    @Query("SELECT COUNT(c) FROM ChatRoom c WHERE c.productId = :productId AND c.bidderId = :bidderId")
    Long countByProductIdAndBidderId(@Param("productId") int productId, @Param("bidderId") String bidderId);

    default boolean existsByProductIdAndBidderId(int productId, String bidderId) {
        return countByProductIdAndBidderId(productId, bidderId) > 0;
    }
}
