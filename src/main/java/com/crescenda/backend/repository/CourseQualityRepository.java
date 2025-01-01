package com.crescenda.backend.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crescenda.backend.model.CourseQuality;

public interface CourseQualityRepository extends JpaRepository<CourseQuality, Long>{

	Optional<CourseQuality> findByTaskTaskId(Long taskId);

}
