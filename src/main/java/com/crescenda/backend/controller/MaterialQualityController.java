package com.crescenda.backend.controller;

import com.crescenda.backend.exception.ResourceNotFoundException;
import com.crescenda.backend.model.MaterialQuality;
import com.crescenda.backend.request.MaterialQualityRequest;
import com.crescenda.backend.response.MaterialQualityResponse;
import com.crescenda.backend.service.MaterialQualityService;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/qa/material/reviews")
public class MaterialQualityController {

    @Autowired
    private MaterialQualityService materialQualityService;

    @PostMapping("/add")
    public ResponseEntity<?> addReview(
            @RequestParam Long draftId,
            @RequestBody MaterialQualityRequest request) {
        try {
            String message = materialQualityService.addReview(draftId, request);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving review: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<MaterialQualityResponse>> getAllMaterialQualities() {
        return ResponseEntity.ok(materialQualityService.getAllMaterialQualities());
    }

    @GetMapping("/{materialId}")
    public ResponseEntity<MaterialQuality> getMaterialQualityByMaterialId(@PathVariable Long materialId) {
        return ResponseEntity.ok(materialQualityService.getMaterialQualityByMaterialId(materialId));
    }

    @PutMapping("/{materialId}")
    public ResponseEntity<String> updateMaterialQuality(
            @PathVariable Long materialId,
            @RequestBody MaterialQualityRequest request) {
        try {
            String message = materialQualityService.updateMaterialQuality(materialId, request);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating review: " + e.getMessage());
        }
    }
}
