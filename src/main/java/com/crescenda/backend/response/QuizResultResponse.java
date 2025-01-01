package com.crescenda.backend.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizResultResponse {
    private int totalQuestions;
    private int correctAnswers;
    private String status; // Pass/Fail
    private LocalDate attemptDate;
    private Long attemptId;
}