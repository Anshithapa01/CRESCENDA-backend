package com.crescenda.backend.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class SubCategory {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subcategoryId;

    @Column(name = "subcategory_name")
    private String subcategoryName;
    
    @Column(name = "sub_category_description")
    private String subCategoryDescription;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

	public SubCategory(Long subcategoryId, String subcategoryName, String subCategoryDescription, Boolean isDeleted) {
		super();
		this.subcategoryId = subcategoryId;
		this.subcategoryName = subcategoryName;
		this.subCategoryDescription = subCategoryDescription;
		this.isDeleted = isDeleted;
	}

	
	//Relationships
	
	@OneToMany(mappedBy = "subCategory")
    @JsonManagedReference("subcategory-draft")  
    private List<Draft> drafts;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference("category-subcategory")  
    private Category category;
}
