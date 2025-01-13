package com.crescenda.backend.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Payment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "payment_date")
	private LocalDateTime paymentDate = LocalDateTime.now();
	
	private double amount;
	
	@Column(name = "payment_status")
	private String paymentStatus; //failed, completed
	
	@Column(name = "payment_id")
	private String paymentId;

		
	public Payment(Long id, LocalDateTime paymentDate, double amount, String paymentStatus,
			String paymentId) {
		super();
		this.id = id;
		this.paymentDate = paymentDate;
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
	
	@OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Enrollment enrollment;
	
}
