package com.crescenda.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizResponse {
    private Long quizId;
    private String question;
    private String answer;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
}
