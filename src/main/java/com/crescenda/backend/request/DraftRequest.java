package com.crescenda.backend.request;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DraftRequest {

    private String courseName;

    private BigDecimal coursePrice;

    private BigDecimal sellingPrice;

    private String courseDescription;

    private String authorNote;
    private String specialNote;
    private String coursePrerequisite;
    private String level;
    private String language;
    private String type;
    private Long subCategoryId;
    private Long mentorId;
    private String thumbnail; 
}
