package com.crescenda.backend.model;

import java.time.LocalDate;

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
public class MentorRequest {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "request_id")
    private Long requestId;

    @Column(name = "reason")
    private String reason;

    @Column(name = "request_date")
    private LocalDate requestDate;

    @Column(name = "status")
    private String status; //pending, picked,completed

    @Column(name = "expert_id")
    private Integer expertId;

    @Column(name = "attachment_url")
    private String attachmentUrl;

	public MentorRequest(Long requestId, String reason, LocalDate requestDate, String status, Integer expertId,
			String attachmentUrl) {
		super();
		this.requestId = requestId;
		this.reason = reason;
		this.requestDate = requestDate;
		this.status = status;
		this.expertId = expertId;
		this.attachmentUrl = attachmentUrl;
	}

    
	//Relationship
    
    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;
}
