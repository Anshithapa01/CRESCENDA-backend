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
public class Sessions {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "session_id")
    private Long sessionId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name  = "session_token")
    private String sessionToken;

	public Sessions(Long sessionId, LocalDateTime createdAt, LocalDateTime updatedAt, String sessionToken) {
		super();
		this.sessionId = sessionId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.sessionToken = sessionToken;
	}

	//mapping 
	
	@ManyToOne
    @JoinColumn(name = "student_id")
	@JsonBackReference("student-session")
    private Student student;

}
