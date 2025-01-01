package com.crescenda.backend.response;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingResponse {
    private Long ratingId;
    private Integer rating;
    private String reviewText;
    private LocalDate createdAt;
    private Long studentId; // To reference the student if needed
    private String studentName; 
    private String parentName;
    private Long parentId;
    private Long rootId;

}