package com.crescenda.backend.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {
    private Long categoryId;
    private String categoryName;
    private String categoryDescription;
    private Boolean isDeleted;

    private String parentCategoryName; // To avoid circular references, only include parent ID
    private Long parentCategoryId;
    private List<SubCategoryResponse> subCategories; // Include subcategories as a list of responses
}

