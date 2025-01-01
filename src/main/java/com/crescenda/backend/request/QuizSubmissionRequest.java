package com.crescenda.backend.request;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmissionRequest {
    private Long courseId;
    private Long studentId;
    private Map<Long, String> answers; 
}