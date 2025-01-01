package com.crescenda.backend.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubCategoryResponse {
	
    private Long subcategoryId;
    private String subcategoryName;
    private String subCategoryDescription;
    private String catetoryName;
    private Boolean isDeleted;

    private Long categoryId; // Include only the category ID to avoid loops
}
