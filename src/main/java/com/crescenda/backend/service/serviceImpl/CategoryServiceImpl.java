package com.crescenda.backend.service.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crescenda.backend.model.Category;
import com.crescenda.backend.repository.CategoryRepository;
import com.crescenda.backend.request.CategoryRequest;
import com.crescenda.backend.response.CategoryResponse;
import com.crescenda.backend.response.SubCategoryResponse;
import com.crescenda.backend.service.CategoryService;

import jakarta.transaction.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    @Transactional
    @Override
    public Category saveCategory(CategoryRequest categoryRequest) {
        // Check if a category with the same name (case-insensitive) already exists
        categoryRepository.findByCategoryNameIgnoreCase(categoryRequest.getName())
            .ifPresent(existingCategory -> {
                throw new IllegalArgumentException("Category with name '" + categoryRequest.getName() + "' already exists.");
            });

        // Initialize a new Category instance
        Category category = new Category();
        category.setCategoryName(categoryRequest.getName());
        category.setCategoryDescription(categoryRequest.getDescription());
        category.setIsDeleted(false); 
        

        // Check if a parent category name is provided
        if (categoryRequest.getParentCategory() != null) {
            categoryRepository.findById(Long.valueOf(categoryRequest.getParentCategory()))
                .ifPresentOrElse(
                    parent -> category.setParentCategory(parent),
                    () -> { throw new IllegalArgumentException("Parent category with ID '" + categoryRequest.getParentCategory() + "' not found."); }
                );
        }
        

        // Save and return the new category
        return categoryRepository.save(category);
    }

    
    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::mapToCategoryResponse) // Map each Category to CategoryResponse
                .toList();
    }

    public CategoryResponse mapToCategoryResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setCategoryId(category.getCategoryId());
        response.setCategoryName(category.getCategoryName());
        response.setCategoryDescription(category.getCategoryDescription());
        response.setIsDeleted(category.getIsDeleted());
        if(category.getParentCategory()!=null) {
        	response.setParentCategoryName(category.getParentCategory().getCategoryName());
        	response.setParentCategoryId(category.getParentCategory().getCategoryId());
        }

        // Map subcategories if they exist
        if (category.getSubCategories() != null) {
            List<SubCategoryResponse> subCategoryResponses = category.getSubCategories().stream()
                    .map(subCategory -> {
                        SubCategoryResponse subResponse = new SubCategoryResponse();
                        subResponse.setSubcategoryId(subCategory.getSubcategoryId());
                        subResponse.setSubcategoryName(subCategory.getSubcategoryName());
                        subResponse.setSubCategoryDescription(subCategory.getSubCategoryDescription());
                        subResponse.setIsDeleted(subCategory.getIsDeleted());
                        return subResponse;
                    })
                    .toList();
            response.setSubCategories(subCategoryResponses);
        }

        return response;
    }

    
    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }


    @Transactional
    @Override
    public Category updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setCategoryName(categoryRequest.getName());
        category.setCategoryDescription(categoryRequest.getDescription());

        if (categoryRequest.getParentCategory() != null) {
            categoryRepository.findById(categoryRequest.getParentCategory())
                .ifPresentOrElse(
                    parent -> category.setParentCategory(parent),
                    () -> { throw new IllegalArgumentException("Parent category with ID '" + categoryRequest.getParentCategory() + "' not found."); }
                );
        } else {
            category.setParentCategory(null); // Remove parent category association if null
        }

        return categoryRepository.save(category);
    }


    @Override
    public Category toggleBlockStatus(Long categoryId, boolean isBlock) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setIsDeleted(isBlock);
            return categoryRepository.save(category);
        } else {
            throw new RuntimeException("Category not found with id: " + categoryId);
        }
    }
   
    @Transactional
    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setIsDeleted(true); // Soft delete
        categoryRepository.save(category);
    }
}