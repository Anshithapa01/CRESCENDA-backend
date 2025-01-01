package com.crescenda.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crescenda.backend.model.SubCategory;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long>{

	List<SubCategory> findByCategoryCategoryId(Long categoryId);
	
	List<SubCategory> findByCategory_CategoryId(Long categoryId);
	
	Optional<SubCategory> findBySubcategoryNameIgnoreCase(String categoryName);
}
