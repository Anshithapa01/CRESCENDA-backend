package com.crescenda.backend.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialQualityRequest {
    private Long materialId; // ID of the material being reviewed
    private String comment;
    private String expertId;
    private String status; // approved, rejected, etc.
}