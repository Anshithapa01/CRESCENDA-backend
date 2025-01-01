package com.crescenda.backend.service;

import java.util.List;

import com.crescenda.backend.model.MaterialQuality;
import com.crescenda.backend.request.MaterialQualityRequest;
import com.crescenda.backend.response.MaterialQualityResponse;

public interface MaterialQualityService {

	String addReview(Long draftId, MaterialQualityRequest request);

	List<MaterialQualityResponse> getAllMaterialQualities();

	MaterialQuality getMaterialQualityByMaterialId(Long materialId);

	String updateMaterialQuality(Long materialId, MaterialQualityRequest request);

}
