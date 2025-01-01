package com.crescenda.backend.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QuizRequest {
	
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String answer;
    private Long draft; 
}