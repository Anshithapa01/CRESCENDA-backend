package com.crescenda.backend.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchasedCourseResponse {
	
	private Long enrollmentId;
    private LocalDateTime enrollmentDate;
    private double amount;
    private String paymentStatus;
    private String paymentId;
    private CourseResponse course;  
    private StudentResponse student; 

}
