package com.crescenda.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

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

@Getter
@Setter
@NoArgsConstructor
@Entity
public class MaterialQuality {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "material_quality_id")
    private Long materialQualityId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "expert_id")
    private String expertId;
    
    @Column(name = "status")
    private String status; //pending, approved, rejected, need improvement

	public MaterialQuality(Long materialQualityId, String comment, String expertId, String status) {
		super();
		this.materialQualityId = materialQualityId;
		this.comment = comment;
		this.expertId = expertId;
		this.status = status;
	}

	//Relationships
	
	@ManyToOne
    @JoinColumn(name = "course_quality_review_id")
	@JsonBackReference("courseQuality-materialQuality")
    private CourseQuality courseQuality;
	
	@OneToOne
    @JoinColumn(name = "material_id") 
	@JsonBackReference("material-materialQuality")
    private Material material;

}
