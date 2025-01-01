package com.crescenda.backend.service;

import java.util.List;

import com.crescenda.backend.model.Material;
import com.crescenda.backend.request.MaterialRequest;
import com.crescenda.backend.response.MaterialResponse;

public interface MaterialService {

	Material getMaterialById(Long materialId);

	void deleteMaterial(Long materialId);

	List<MaterialResponse> getMaterialsByChapterId(Long chapterId);

	Material addMaterial(Long chapterId, MaterialRequest materialRequest);

	Material updateMaterial(Long materialId, MaterialRequest materialRequest);

}
