package com.crescenda.backend.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Category {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category_description")
    private String categoryDescription;

    @Column(name = "is_deleted")
    private Boolean isDeleted;


	public Category(Long categoryId, String categoryName, String categoryDescription, Boolean isDeleted) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.categoryDescription = categoryDescription;
		this.isDeleted = isDeleted;
	}
    
    
	//Relationships
    
        
    @ManyToOne
    private Category parentCategory;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference("category-subcategory")  // Corresponds to the unique reference name in SubCategory
    private List<SubCategory> subCategories;
}
