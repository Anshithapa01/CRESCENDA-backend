package com.crescenda.backend.response;

import com.crescenda.backend.model.MaterialQuality;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaterialQualityResponse {

	private Long materialQualityId;
    private String comment;
    private String expertId;
    private String status;
    private Long materialId; // Simplify Material reference
    private Long courseQualityId; // Simplify CourseQuality reference
    
    public MaterialQualityResponse(MaterialQuality materialQuality) {
        this.materialQualityId = materialQuality.getMaterialQualityId();
        this.comment = materialQuality.getComment();
        this.expertId = materialQuality.getExpertId();
        this.status = materialQuality.getStatus();
        if (materialQuality.getMaterial() != null) {
            this.materialId = materialQuality.getMaterial().getMaterialId();
        }
        if (materialQuality.getCourseQuality() != null) {
            this.courseQualityId = materialQuality.getCourseQuality().getCourseQualityId();
        }
    }

}
