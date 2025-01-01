package com.crescenda.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crescenda.backend.model.MaterialQuality;

public interface MaterialQualityRepository extends JpaRepository<MaterialQuality, Long> {
	Optional<MaterialQuality> findByMaterialMaterialId(Long materialId);

	List<MaterialQuality> findByCourseQualityCourseQualityId(Long courseQualityId);
}