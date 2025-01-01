package com.crescenda.backend.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubCategoryRequest {

	private String name;
    private String description;
    private Long categoryId;
}
