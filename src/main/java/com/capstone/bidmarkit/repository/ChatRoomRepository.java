package com.capstone.bidmarkit.repository;

import com.capstone.bidmarkit.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT cr.productId FROM ChatRoom cr WHERE cr.id = :roomId")
    int findProductIdById(@Param("roomId") int productId);
}
