package com.crescenda.backend.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CourseQuality {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "course_quality_id")
    private Long courseQualityId;

    @Column(name = "status")
    private String status;  //pending, approved, rejected, need improvement

    @Column(name = "feedback")
    private String feedback;

	public CourseQuality(Long courseQualityId, String status, String feedback) {
		super();
		this.courseQualityId = courseQualityId;
		this.status = status;
		this.feedback = feedback;
	}

	//Relationships
	
	@OneToOne
	@JoinColumn(name = "task_id")
	@JsonBackReference("task-courseQuality")
	private Task task;
    
    @OneToMany(mappedBy = "courseQuality", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("courseQuality-materialQuality")
    private List<MaterialQuality> materialQualities;
}
