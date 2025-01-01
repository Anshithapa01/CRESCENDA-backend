package com.crescenda.backend.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.crescenda.backend.model.Chapter;
import com.crescenda.backend.model.Material;
import com.crescenda.backend.repository.ChapterRepository;
import com.crescenda.backend.repository.MaterialRepository;
import com.crescenda.backend.request.MaterialRequest;
import com.crescenda.backend.response.ChapterResponse;
import com.crescenda.backend.response.MaterialResponse;
import com.crescenda.backend.service.MaterialService;

@Service
public class MaterialServiceImpl implements MaterialService{

	private final MaterialRepository materialRepository;
    private final ChapterRepository chapterRepository;

    public MaterialServiceImpl(MaterialRepository materialRepository, ChapterRepository chapterRepository) {
        this.materialRepository = materialRepository;
        this.chapterRepository = chapterRepository;
    }

    @Override
    public List<MaterialResponse> getMaterialsByChapterId(Long chapterId) {
        List<Material> materials = materialRepository.findByChapter_ChapterId(chapterId);

        // Map entities to DTOs
        return materials.stream()
                .map(material -> new MaterialResponse(
                        material.getMaterialId(),
                        new ChapterResponse(
                                material.getChapter().getChapterId(),
                                material.getChapter().getChapterName(),
                                material.getChapter().getChapterDescription()
                        ),
                        material.getMaterialName(),
                        material.getMaterialType(),
                        material.getMaterialUrl()
                ))
                .collect(Collectors.toList());
    }


    @Override
    public Material addMaterial(Long chapterId, MaterialRequest materialRequest) {
        Chapter chapter = chapterRepository.findById(chapterId)
            .orElseThrow(() -> new RuntimeException("Chapter not found with ID: " + chapterId));

        Material material = new Material();
        material.setMaterialName(materialRequest.getMaterialName());
        material.setMaterialType(materialRequest.getMaterialType());
        material.setMaterialUrl(materialRequest.getMaterialUrl());
        material.setChapter(chapter);

        return materialRepository.save(material);
    }

    @Override
    public Material updateMaterial(Long materialId, MaterialRequest materialRequest) {
        Material existingMaterial = materialRepository.findById(materialId)
            .orElseThrow(() -> new RuntimeException("Material not found with ID: " + materialId));
        System.out.println("materialRequest.getChapterId()"+materialRequest.getChapterId());
        Chapter chapter = chapterRepository.findById(materialRequest.getChapterId())
            .orElseThrow(() -> new RuntimeException("Chapter not found with ID: " + materialRequest.getChapterId()));

        existingMaterial.setMaterialName(materialRequest.getMaterialName());
        existingMaterial.setMaterialType(materialRequest.getMaterialType());
        existingMaterial.setMaterialUrl(materialRequest.getMaterialUrl());
        existingMaterial.setChapter(chapter);

        return materialRepository.save(existingMaterial);
    }


    @Override
    public void deleteMaterial(Long materialId) {
        if (!materialRepository.existsById(materialId)) {
            throw new RuntimeException("Material not found with ID: " + materialId);
        }
        materialRepository.deleteById(materialId);
    }

    @Override
    public Material getMaterialById(Long materialId) {
        return materialRepository.findById(materialId)
            .orElseThrow(() -> new RuntimeException("Material not found with ID: " + materialId));
    }
}
