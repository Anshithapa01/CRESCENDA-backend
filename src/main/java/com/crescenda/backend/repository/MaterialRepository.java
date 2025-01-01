package com.crescenda.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crescenda.backend.model.Material;

public interface MaterialRepository extends JpaRepository<Material, Long>{
    List<Material> findByChapter_ChapterId(Long chapterId);
}
