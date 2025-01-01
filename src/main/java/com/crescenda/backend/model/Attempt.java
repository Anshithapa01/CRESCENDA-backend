package com.crescenda.backend.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
public class Attempt {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "attempt_id")
    private Long attemptId;

    @Column(name = "attempt_date")
    private LocalDate attemptDate;

    @Column(name = "score")
    private Integer score;

    @Column(name = "status")
    private String status; //pass, fail

    @Column(name = "total_question")
    private Integer totalQuestion;

	public Attempt(Long attemptId, LocalDate attemptDate, Integer score, String status, Integer totalQuestion) {
		super();
		this.attemptId = attemptId;
		this.attemptDate = attemptDate;
		this.score = score;
		this.status = status;
		this.totalQuestion = totalQuestion;
	}
	
	
	//Relationships
    
    @ManyToOne
    @JoinColumn(name = "draft_id", nullable = false) 
    @JsonBackReference("draft-studentAttempts")
    private Draft draft;
    
	@ManyToOne
    @JoinColumn(name = "student_id")
	@JsonBackReference("student-attempts")
    private Student student;

}
