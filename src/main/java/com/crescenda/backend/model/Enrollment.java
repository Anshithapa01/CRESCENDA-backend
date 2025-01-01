package com.crescenda.backend.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Enrollment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "enrollment_id")
	private int enrollmentId;
	
	@Column(name = "enrollment_date")
	private LocalDateTime enrollmentDate = LocalDateTime.now();
	
	private double amount;
	
	@Column(name = "payment_status")
	private String paymentStatus; //pending, completed
	
	@Column(name = "purchase_confirmation")
	private String purchaseConfirmation;
	
	@Column(name = "payment_id")
	private String paymentId;

		
	public Enrollment(int enrollmentId, LocalDateTime enrollmentDate, double amount, String paymentStatus,
			String paymentId) {
		super();
		this.enrollmentId = enrollmentId;
		this.enrollmentDate = enrollmentDate;
		this.amount = amount;
		this.paymentStatus = paymentStatus;
		this.paymentId = paymentId;
	}
	
	
	//Relationships
	
	@ManyToOne
    @JoinColumn(name = "student_id") 
	@JsonBackReference("student-enrollment")
    private Student student;
	
	@ManyToOne
	@JoinColumn(name = "course_id") 
	private Course course;
	
	
}
