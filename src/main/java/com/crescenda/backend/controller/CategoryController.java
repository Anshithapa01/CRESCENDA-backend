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

import com.crescenda.backend.model.Category;
import com.crescenda.backend.request.CategoryRequest;
import com.crescenda.backend.response.CategoryResponse;
import com.crescenda.backend.service.CategoryService;
import com.crescenda.backend.request.BlockRequest;

@RestController
@RequestMapping("/api/admin/category")
public class CategoryController {
	
	@Autowired
    private CategoryService categoryService;
	
	@PostMapping("/add")
	public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest categoryRequest) {

        Category savedCategory = categoryService.saveCategory(categoryRequest);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }
	
	@GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(category -> new ResponseEntity<>(category, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        Category updatedCategory = categoryService.updateCategory(id, categoryRequest);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }
    
    @PutMapping("/block/{id}")
    public Category toggleBlockCategory(@PathVariable Long id, @RequestBody BlockRequest blockRequest) {
        return categoryService.toggleBlockStatus(id, blockRequest.isDeleted());
    }

}
