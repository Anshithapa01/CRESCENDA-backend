package com.crescenda.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crescenda.backend.request.DraftRequest;
import com.crescenda.backend.response.DraftResponse;
import com.crescenda.backend.service.DraftService;

@RestController
@RequestMapping("/api/mentor/draft")
public class DraftController {
	
	@Autowired
    private DraftService draftService;

	@PostMapping("/add")
	public ResponseEntity<String> addDraft(@RequestBody DraftRequest draftRequestDTO) {
        draftService.createDraftFromDTO(draftRequestDTO);
        return ResponseEntity.ok("Draft added successfully");
    }
	
	@GetMapping
    public ResponseEntity<List<DraftResponse>> getDraftsByMentorId(@RequestParam Long mentorId) {
        List<DraftResponse> drafts = draftService.getDraftsByMentorId(mentorId);
        return ResponseEntity.ok(drafts);
    }

    // Get a particular draft by ID
    @GetMapping("/{draftId}")
    public ResponseEntity<DraftResponse> getDraftById(@PathVariable Long draftId) {
        DraftResponse draft = draftService.getDraftResponseById(draftId);
        return ResponseEntity.ok(draft);
    }

    // Delete a specific draft
    @DeleteMapping("/{draftId}")
    public ResponseEntity<String> deleteDraft(@PathVariable Long draftId) {
        draftService.deleteDraftById(draftId);
        return ResponseEntity.ok("Draft deleted successfully");
    }

    // Publish a specific draft (change status to "pending")
    @PostMapping("/{draftId}/publish")
    public ResponseEntity<String> publishDraft(@PathVariable Long draftId) {
        draftService.publishDraft(draftId);
        return ResponseEntity.ok("Task created'"); 
    }

    // Update a specific draft
    @PutMapping("/{draftId}")
    public ResponseEntity<String> updateDraft(@PathVariable Long draftId, @RequestBody DraftRequest draftRequestDTO) {
        draftService.updateDraft(draftId, draftRequestDTO);
        return ResponseEntity.ok("Draft updated successfully");
    }
    
    @GetMapping("/pending/first")
    public ResponseEntity<DraftResponse> getFirstPendingDraft(@RequestParam Long qaId) {
        DraftResponse pendingDraft = draftService.getFirstPendingDraft(qaId);
        if (pendingDraft != null) {
            return ResponseEntity.ok(pendingDraft);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{mentorId}/active")
    public ResponseEntity<List<DraftResponse>> getApprovedDraftsByMentor(@PathVariable Long mentorId) {
        List<DraftResponse> drafts = draftService.getApprovedDraftsByMentor(mentorId);
        return ResponseEntity.ok(drafts);
    }
	
}
