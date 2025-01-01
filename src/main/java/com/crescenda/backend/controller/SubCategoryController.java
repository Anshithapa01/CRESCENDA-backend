package com.crescenda.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crescenda.backend.model.SubCategory;
import com.crescenda.backend.request.BlockRequest;
import com.crescenda.backend.request.SubCategoryRequest;
import com.crescenda.backend.response.SubCategoryResponse;
import com.crescenda.backend.service.SubCategoryService;

@RestController
@RequestMapping("/api/admin/category/{categoryId}/subcategories")
public class SubCategoryController {
	
	@Autowired
    private SubCategoryService subCategoryService;

	@GetMapping
	public ResponseEntity<List<SubCategoryResponse>> getSubCategories(@PathVariable Long categoryId) {
	    List<SubCategoryResponse> subCategories = subCategoryService.getSubCategoriesByCategoryId(categoryId);
	    return ResponseEntity.ok(subCategories); // Send the DTOs in the response
	}

    // Get a specific subcategory by its ID
	@GetMapping("/{subCategoryId}")
    public ResponseEntity<SubCategory> getSubCategory(@PathVariable String subCategoryId) {
		System.out.println("subCategoryId"+subCategoryId);
        SubCategory subCategory = subCategoryService.getSubCategoryById(Long.valueOf(subCategoryId));
        return ResponseEntity.ok(subCategory);
    }

    // Add a new subcategory 
    @PostMapping
    public ResponseEntity<SubCategory> addSubCategory(@PathVariable Long categoryId, @RequestBody SubCategoryRequest subCategoryRequest) {
        SubCategory savedSubCategory = subCategoryService.addSubCategory(categoryId, subCategoryRequest);  // Move logic to service
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSubCategory);
    }


    // Update an existing subcategory
    @PutMapping("/{id}")
    public ResponseEntity<SubCategory> updateSubCategory(
            @PathVariable Long id,
            @RequestBody SubCategoryRequest subCategoryRequest) {

        // Fetch the existing SubCategory by ID
        SubCategory existingSubCategory = subCategoryService.getSubCategoryById(id);
        if (existingSubCategory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Update only the fields allowed for modification
        existingSubCategory.setSubcategoryName(subCategoryRequest.getName());
        existingSubCategory.setSubCategoryDescription(subCategoryRequest.getDescription());

        // Save the updated subcategory
        SubCategory updatedSubCategory = subCategoryService.updateSubCategory(id, existingSubCategory);
        return ResponseEntity.ok(updatedSubCategory);
    }



    @PutMapping("/block/{id}")
    public SubCategory toggleBlockCategory(@PathVariable Long id, @RequestBody BlockRequest blockRequest) {
        return subCategoryService.toggleBlockStatus(id, blockRequest.isDeleted());
    }
    
   
}
