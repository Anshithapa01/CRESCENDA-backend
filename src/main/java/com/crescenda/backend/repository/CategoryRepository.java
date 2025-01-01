package com.crescenda.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crescenda.backend.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByCategoryNameIgnoreCase(String categoryName);
}