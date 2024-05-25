package com.capstone.bidmarkit.repository;

import com.capstone.bidmarkit.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT cr.productId FROM ChatRoom cr WHERE cr.id = :roomId")
    int findProductIdById(@Param("roomId") int productId);

    @Modifying
    @Transactional
    @Query("UPDATE ChatRoom c SET c.updatedAt = CURRENT_TIMESTAMP WHERE c.id = :chatRoomId")
    void updateUpdatedAt(int chatRoomId);
}
