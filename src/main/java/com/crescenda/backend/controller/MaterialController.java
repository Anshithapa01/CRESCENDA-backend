package com.crescenda.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crescenda.backend.model.Material;
import com.crescenda.backend.request.MaterialRequest;
import com.crescenda.backend.response.MaterialResponse;
import com.crescenda.backend.service.MaterialService;

@RestController
@RequestMapping("/api/mentor/draft/chapter/material")
public class MaterialController {
    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping("/{chapterId}")
    public ResponseEntity<List<MaterialResponse>> getMaterialsByChapterId(@PathVariable Long chapterId) {
        List<MaterialResponse> materials = materialService.getMaterialsByChapterId(chapterId);
        return ResponseEntity.ok(materials);
    }

    // Modified to use MaterialRequest DTO
    @PostMapping("/{chapterId}/add")
    public ResponseEntity<Material> addMaterial(@PathVariable Long chapterId, @RequestBody MaterialRequest materialRequest) {
        Material newMaterial = materialService.addMaterial(chapterId, materialRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMaterial);
    }

    // Modified to use MaterialRequest DTO
    @PutMapping("/{materialId}/update")
    public ResponseEntity<Material> updateMaterial(@PathVariable Long materialId, @RequestBody MaterialRequest updatedMaterialRequest) {
        Material updated = materialService.updateMaterial(materialId, updatedMaterialRequest);
        System.out.println(updated.toString());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{materialId}/delete")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long materialId) {
        materialService.deleteMaterial(materialId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{materialId}/get")
    public ResponseEntity<Material> getMaterialById(@PathVariable Long materialId) {
        Material material = materialService.getMaterialById(materialId);
        return ResponseEntity.ok(material);
    }
}
