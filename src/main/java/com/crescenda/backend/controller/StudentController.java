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
import com.crescenda.backend.model.Student;
import com.crescenda.backend.response.StudentResponse;
import com.crescenda.backend.service.StudentService;

@RestController
@RequestMapping("/api/student")
public class StudentController {
	
	@Autowired
	private StudentService studentService;
	
	@GetMapping("/profile")
	public ResponseEntity<StudentResponse> getStudentProfileHandler(@RequestHeader("Authorization") String jwt) throws UserException {
	    StudentResponse studentResponse = studentService.findUserProfileResponseByJwt(jwt);
	    return new ResponseEntity<>(studentResponse, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable long id) {
        StudentResponse studentResponse = studentService.getStudentById(id);
        if (studentResponse != null) {
            return ResponseEntity.ok(studentResponse);
        }
        return ResponseEntity.notFound().build();
    }

	@GetMapping
    public List<StudentResponse> getAllStudents() {
		return studentService.getAllStudents();
	}
	
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudentById(
            @PathVariable long id, 
            @RequestBody Student updatedStudent) {
        StudentResponse studentResponse = studentService.updateStudentById(id, updatedStudent);
        if (studentResponse != null) {
            return ResponseEntity.ok(studentResponse);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/block")
    public ResponseEntity<String> blockQA(@PathVariable Long id) {
    	studentService.setBlockedStatus(id, true);
        return ResponseEntity.ok("QA blocked successfully.");
    }

    // Unblock a QA
    @PutMapping("/{id}/unblock")
    public ResponseEntity<String> unblockQA(@PathVariable Long id) {
    	studentService.setBlockedStatus(id, false);
        return ResponseEntity.ok("QA unblocked successfully.");
    }
}
