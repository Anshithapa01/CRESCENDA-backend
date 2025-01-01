package com.crescenda.backend.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class Task {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "task_id")
    private Long taskId;

    @Column(name = "status")
    private String status; //pending, picked, completed

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "qa_expert_uid")
    private String qaExpertUID;

	public Task(Long taskId, String status, LocalDate createdAt, String qaExpertUID) {
		super();
		this.taskId = taskId;
		this.status = status;
		this.createdAt = createdAt;
		this.qaExpertUID = qaExpertUID;
	}
     
	
	//Relationships
    @ManyToOne
    @JoinColumn(name = "qa_expert_id")
    @JsonBackReference("qa-task")
    private QA qa;
    
    @OneToOne
    @JoinColumn(name = "draft_id")
    @JsonBackReference("draft-task")  // Match the reference name in Draft
    private Draft draft;
	
    @OneToOne(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("task-courseQuality")
    private CourseQuality courseQuality;
   
}
