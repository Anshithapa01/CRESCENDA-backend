package com.crescenda.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crescenda.backend.response.MentorResponse;
import com.crescenda.backend.response.MentorStudentResponse;
import com.crescenda.backend.service.MentorStudentService;

@RestController
@RequestMapping("/api/mentor")
public class MentorStudentController {

    @Autowired
    private MentorStudentService mentorStudentService;

    // Get all mentors for a student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<MentorStudentResponse>> getAllMentors(@PathVariable Long studentId) {
        List<MentorStudentResponse> mentors = mentorStudentService.getAllMentorsByStudentId(studentId);
        return ResponseEntity.ok(mentors);
    }

    // Search mentors for a student by query
    @GetMapping("/student/{studentId}/search")
    public ResponseEntity<List<MentorStudentResponse>> searchMentors(
            @PathVariable Long studentId,
            @RequestParam String query) {
        List<MentorStudentResponse> mentors = mentorStudentService.searchMentorsByStudentIdAndQuery(studentId, query);
        return ResponseEntity.ok(mentors);
    }
}