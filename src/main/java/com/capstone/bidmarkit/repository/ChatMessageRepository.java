package com.capstone.bidmarkit.repository;

import com.capstone.bidmarkit.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
