package com.crescenda.backend.service;

import java.util.List;
import java.util.Optional;

import com.crescenda.backend.model.SubCategory;
import com.crescenda.backend.request.SubCategoryRequest;
import com.crescenda.backend.response.SubCategoryResponse;

public interface SubCategoryService {

	List<SubCategoryResponse> getSubCategoriesByCategoryId(Long categoryId);

	SubCategory updateSubCategory(Long id, SubCategory subCategory);


	SubCategory addSubCategory(Long categoryId, SubCategoryRequest subCategoryRequest);

	SubCategory toggleBlockStatus(Long categoryId, boolean isBlock);

	SubCategory getSubCategoryById(Long subCategoryId);

	

}
