package com.capstone.bidmarkit.repository;

import com.capstone.bidmarkit.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
