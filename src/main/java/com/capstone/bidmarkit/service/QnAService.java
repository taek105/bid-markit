package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.Answer;
import com.capstone.bidmarkit.domain.Product;
import com.capstone.bidmarkit.domain.Question;
import com.capstone.bidmarkit.dto.AddAnswerRequest;
import com.capstone.bidmarkit.dto.AddQuestionRequest;
import com.capstone.bidmarkit.dto.PushAlarmRequest;
import com.capstone.bidmarkit.repository.AnswerRepository;
import com.capstone.bidmarkit.repository.ProductRepository;
import com.capstone.bidmarkit.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QnAService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final ProductRepository productRepository;
    private final PushService pushService;

    public void saveQuestion(String memberId, AddQuestionRequest request) {
        Product found = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new IllegalArgumentException("Product not found")
        );

        questionRepository.save(
                Question.builder()
                        .content(request.getContent())
                        .memberId(memberId)
                        .product(found)
                        .build()
        );

        pushService.pushAlarm(PushAlarmRequest.builder()
                .productName(found.getName())
                .imgURL(found.getImages().get(0).getImgUrl())
                .memberId(found.getMemberId())
                .type(1)
                .build()
        );
    }

    public void saveAnswer(String memberId, AddAnswerRequest request) throws IllegalAccessException {
        Question found = questionRepository.findById(request.getQuestionId()).orElseThrow(
                () -> new IllegalArgumentException("Question not found")
        );
        if (!found.getProduct().getMemberId().equals(memberId))
            throw new IllegalAccessException("Only seller can write answer");

        answerRepository.save(
                Answer.builder()
                        .question(found)
                        .content(request.getContent())
                        .build()
        );

        pushService.pushAlarm(PushAlarmRequest.builder()
                .productName(found.getProduct().getName())
                .imgURL(found.getProduct().getImages().get(0).getImgUrl())
                .memberId(found.getMemberId())
                .type(2)
                .build()
        );
    }
}
