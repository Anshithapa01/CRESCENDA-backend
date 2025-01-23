package com.crescenda.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.crescenda.backend.exception.UserException;
import com.crescenda.backend.model.QA;
import com.crescenda.backend.request.QARequest;
import com.crescenda.backend.response.QAResponse;
import com.crescenda.backend.service.QaService;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/qa")
public class QAController {

    @Autowired
    private QaService qaService;

    @GetMapping
    public List<QAResponse> getAllQAs() {
        return qaService.getAllQAs();
    }
    
    @PostMapping
    public ResponseEntity<QAResponse> addQA(@RequestBody QARequest qaRequest) throws UserException {
    	System.out.println("qarequest.toString()"+qaRequest.toString());
        QA savedQA = qaService.saveQA(qaRequest);
        System.out.println("savedQA.toString()"+savedQA.toString());
        return ResponseEntity.ok(qaService.convertToResponse(savedQA));
    }

    // Update QA
    @PutMapping("/{id}")
    public ResponseEntity<QAResponse> updateQA(@PathVariable Long id, @RequestBody QARequest qaDetails) {
        QA updatedQA = qaService.updateQA(id, qaDetails);
        return ResponseEntity.ok(qaService.convertToResponse(updatedQA));
    }

    // Get QA by ID
    @GetMapping("/{id}")
    public ResponseEntity<QAResponse> getQAById(@PathVariable Long id) {
        QAResponse qa = qaService.getQAById(id);
        return ResponseEntity.ok(qa);
    }
    
    @GetMapping("/leads")
    public ResponseEntity<List<QAResponse>> getAllQALeads() {
        List<QAResponse> qaLeads = qaService.getAllQAByRole("QA_Lead");
        return ResponseEntity.ok(qaLeads);
    }
    
    @PutMapping("/{id}/block")
    public ResponseEntity<String> blockQA(@PathVariable Long id) {
        qaService.setBlockedStatus(id, true);
        return ResponseEntity.ok("QA blocked successfully.");
    }

    // Unblock a QA
    @PutMapping("/{id}/unblock")
    public ResponseEntity<String> unblockQA(@PathVariable Long id) {
        qaService.setBlockedStatus(id, false);
        return ResponseEntity.ok("QA unblocked successfully.");
    }
}