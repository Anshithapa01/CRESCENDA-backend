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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Material {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "material_id")
    private Long materialId;

    @Column(name = "material_type")
    private String materialType;

    @Column(name = "material_name")
    private String materialName;

    @Column(name = "material_url")
    private String materialUrl;

    @Column(name = "position")
    private Integer position;

	public Material(Long materialId, String materialType, String materialName, String materialUrl, Integer position) {
		super();
		this.materialId = materialId;
		this.materialType = materialType;
		this.materialName = materialName;
		this.materialUrl = materialUrl;
		this.position = position;
	}
	
    
	//Relationships
	
		@ManyToOne
	    @JoinColumn(name = "chapter_id")
		@JsonBackReference("chapter-material")
	    private Chapter chapter;
		
		@OneToMany(mappedBy = "material")
	    private List<MentorRequest> mentorRequests;
		
		@OneToOne(mappedBy = "material", cascade = CascadeType.ALL, orphanRemoval = true)
		@JsonManagedReference("material-materialQuality")
	    private MaterialQuality materialQuality;
	
	
}
