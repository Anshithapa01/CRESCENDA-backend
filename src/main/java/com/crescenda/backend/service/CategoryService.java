package com.crescenda.backend.service;

import java.util.List;
import java.util.Optional;

import com.crescenda.backend.model.Category;
import com.crescenda.backend.request.CategoryRequest;
import com.crescenda.backend.response.CategoryResponse;

public interface CategoryService {

	Category saveCategory(CategoryRequest categoryRequest);

	void deleteCategory(Long id);

	Category updateCategory(Long id, CategoryRequest categoryRequest);

	Optional<Category> getCategoryById(Long id);

	List<CategoryResponse> getAllCategories();

	Category toggleBlockStatus(Long categoryId, boolean isBlock);

}
