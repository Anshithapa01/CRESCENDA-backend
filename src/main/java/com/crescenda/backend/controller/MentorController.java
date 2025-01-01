package com.crescenda.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crescenda.backend.exception.UserException;
import com.crescenda.backend.model.Mentor;
import com.crescenda.backend.response.MentorResponse;
import com.crescenda.backend.service.MentorService;

@RestController
@RequestMapping("/api/mentor")
public class MentorController {
	
	@Autowired
	private MentorService mentorService;
	
	@GetMapping("/profile")
	public ResponseEntity<MentorResponse> getMentorProfileHandler(@RequestHeader("Authorization") String jwt) throws UserException {
	    MentorResponse mentor = mentorService.findMentorProfileByJwt(jwt);
	    return new ResponseEntity<>(mentor, HttpStatus.ACCEPTED);
	}
	
	@GetMapping
    public ResponseEntity<List<MentorResponse>> getAllMentors() {
        List<MentorResponse> mentors = mentorService.getAllMentors();
        return ResponseEntity.ok(mentors);
    }

    @GetMapping("/{mentorId}")
    public ResponseEntity<MentorResponse> getMentorById(@PathVariable Long mentorId) throws UserException {
        MentorResponse mentor = mentorService.getMentorById(mentorId);
        return ResponseEntity.ok(mentor);
    }
    
    @PutMapping("/{mentorId}")
    public ResponseEntity<MentorResponse> updateMentor(
        @PathVariable Long mentorId,
        @RequestBody Mentor updatedMentor
    ) {
        MentorResponse response = mentorService.updateMentor(mentorId, updatedMentor);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/block")
    public ResponseEntity<String> blockQA(@PathVariable Long id) {
        mentorService.setBlockedStatus(id, true);
        return ResponseEntity.ok("QA blocked successfully.");
    }

    // Unblock a QA
    @PutMapping("/{id}/unblock")
    public ResponseEntity<String> unblockQA(@PathVariable Long id) {
    	mentorService.setBlockedStatus(id, false);
        return ResponseEntity.ok("QA unblocked successfully.");
    }
}
