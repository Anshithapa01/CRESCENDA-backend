package com.crescenda.backend.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crescenda.backend.exception.ResourceNotFoundException;
import com.crescenda.backend.model.CourseQuality;
import com.crescenda.backend.model.Draft;
import com.crescenda.backend.model.Material;
import com.crescenda.backend.model.MaterialQuality;
import com.crescenda.backend.model.QA;
import com.crescenda.backend.repository.CourseQualityRepository;
import com.crescenda.backend.repository.DraftRepository;
import com.crescenda.backend.repository.MaterialQualityRepository;
import com.crescenda.backend.repository.MaterialRepository;
import com.crescenda.backend.repository.QaRepository;
import com.crescenda.backend.request.MaterialQualityRequest;
import com.crescenda.backend.response.MaterialQualityResponse;
import com.crescenda.backend.service.MaterialQualityService;

@Service
public class MaterialQualityServiceImpl implements MaterialQualityService{

	@Autowired
    private MaterialQualityRepository materialQualityRepository;

    @Autowired
    private DraftRepository draftRepository;

    @Autowired
    private CourseQualityRepository courseQualityRepository;

    @Autowired
    private MaterialRepository materialRepository;
    
    @Autowired
    private QaRepository qaRepository;

    @Override
    public String addReview(Long draftId, MaterialQualityRequest request) {
    	String expertId = request.getExpertId();
        if (expertId == null || !expertId.startsWith("QE")) {
            throw new IllegalArgumentException("Invalid Expert ID. Please provide a valid id");
        }

        // Step 2: Verify Expert ID exists in the database
        QA expert = qaRepository.findByQaUid(expertId)
            .orElseThrow(() -> new ResourceNotFoundException("No expert found with ID: " + expertId));

        
        Draft draft = draftRepository.findById(draftId)
                .orElseThrow(() -> new ResourceNotFoundException("Draft not found with id: " + draftId));

        CourseQuality courseQuality = Optional.ofNullable(draft.getTask())
                .map(task -> task.getCourseQuality())
                .orElseThrow(() -> new IllegalStateException("Course quality not found for draft"));

        Material material = materialRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with id: " + request.getMaterialId()));

        MaterialQuality materialQuality = new MaterialQuality();
        materialQuality.setComment(request.getComment());
        materialQuality.setExpertId(expertId);
        materialQuality.setStatus(request.getStatus());
        materialQuality.setCourseQuality(courseQuality);
        materialQuality.setMaterial(material);

        materialQualityRepository.save(materialQuality);
        return "Material review saved successfully!";
    }

    @Override
    public List<MaterialQualityResponse> getAllMaterialQualities() {
    	List<MaterialQuality> qualities = materialQualityRepository.findAll();
        return qualities.stream()
                .map(MaterialQualityResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public MaterialQuality getMaterialQualityByMaterialId(Long materialId) {
        return materialQualityRepository.findByMaterialMaterialId(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("No quality review found for material with id: " + materialId));
    }

    @Override
    public String updateMaterialQuality(Long materialId, MaterialQualityRequest request) {
    	String expertId = request.getExpertId();
        if (expertId == null || !expertId.startsWith("QE")) {
            throw new IllegalArgumentException("Invalid Expert ID. Please provide a valid id");
        }

        // Step 2: Verify Expert ID exists in the database
        QA expert = qaRepository.findByQaUid(expertId)
            .orElseThrow(() -> new ResourceNotFoundException("No expert found with ID: " + expertId));
        
        MaterialQuality materialQuality = materialQualityRepository.findByMaterialMaterialId(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("No quality review found for material with id: " + materialId));

        materialQuality.setComment(request.getComment());
        materialQuality.setExpertId(request.getExpertId());
        materialQuality.setStatus(request.getStatus());
        materialQualityRepository.save(materialQuality);

        return "Material review updated successfully!";
    }
}
