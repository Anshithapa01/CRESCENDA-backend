package com.crescenda.backend.service.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.crescenda.backend.model.Category;
import com.crescenda.backend.model.SubCategory;
import com.crescenda.backend.repository.CategoryRepository;
import com.crescenda.backend.repository.SubCategoryRepository;
import com.crescenda.backend.request.SubCategoryRequest;
import com.crescenda.backend.response.SubCategoryResponse;
import com.crescenda.backend.service.SubCategoryService;

@Service
public class SubCategoryServiceImpl implements SubCategoryService{

	private final SubCategoryRepository subCategoryRepository;

	private final CategoryRepository categoryRepository;

    public SubCategoryServiceImpl(SubCategoryRepository subCategoryRepository,
    		CategoryRepository categoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository=categoryRepository;
    }

    @Override
    public List<SubCategoryResponse> getSubCategoriesByCategoryId(Long categoryId) {
        List<SubCategory> subCategories = subCategoryRepository.findByCategoryCategoryId(categoryId);

        // Map to SubCategoryResponse DTO
        return subCategories.stream()
                .map(subCategory -> {
                    SubCategoryResponse response = new SubCategoryResponse();
                    response.setSubcategoryId(subCategory.getSubcategoryId());
                    response.setSubcategoryName(subCategory.getSubcategoryName());
                    response.setSubCategoryDescription(subCategory.getSubCategoryDescription());
                    response.setCatetoryName(subCategory.getCategory().getCategoryName()); // Include category name
                    response.setCategoryId(subCategory.getCategory().getCategoryId()); // Include category ID
                    response.setIsDeleted(subCategory.getIsDeleted());
                    return response;
                })
                .collect(Collectors.toList());
    }

    
    @Override
    public SubCategory getSubCategoryById(Long subCategoryId) {
        return subCategoryRepository.findById(subCategoryId).orElseThrow(() -> new RuntimeException("SubCategory not found"));
    }

    
    @Override
    public SubCategory addSubCategory(Long categoryId, SubCategoryRequest subCategoryRequest) {
    	subCategoryRepository.findBySubcategoryNameIgnoreCase(subCategoryRequest.getName())
        .ifPresent(existingCategory -> {
            throw new IllegalArgumentException("Category with name '" + subCategoryRequest.getName() + "' already exists.");
        });

        // Fetch the category from the database
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Create a new SubCategory and populate its fields
        SubCategory subCategory = new SubCategory();
        subCategory.setSubcategoryName(subCategoryRequest.getName());
        subCategory.setSubCategoryDescription(subCategoryRequest.getDescription());
        subCategory.setCategory(category);  // Associate the category
        subCategory.setIsDeleted(false);   // Set is_deleted to false by default

        // Save the subcategory and return the saved entity
        return subCategoryRepository.save(subCategory);
    }

    @Override
    public SubCategory updateSubCategory(Long id, SubCategory subCategory) {
    	System.out.println(id+" and "+ subCategory);
        if (subCategoryRepository.existsById(id)) {
            subCategory.setSubcategoryId(id);
            return subCategoryRepository.save(subCategory);
        }
        return null;
    }

    @Override
    public SubCategory toggleBlockStatus(Long categoryId, boolean isBlock) {
        Optional<SubCategory> subCategoryOptional = subCategoryRepository.findById(categoryId);
        if (subCategoryOptional.isPresent()) {
            SubCategory subCategory = subCategoryOptional.get();
            subCategory.setIsDeleted(isBlock);
            return subCategoryRepository.save(subCategory);
        } else {
            throw new RuntimeException("Category not found with id: " + categoryId);
        }
    }

}
