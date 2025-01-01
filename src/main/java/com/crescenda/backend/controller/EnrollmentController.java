package com.crescenda.backend.controller;

import com.crescenda.backend.response.PurchasedCourseResponse;
import com.crescenda.backend.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    // Endpoint to get all purchased courses for a specific student
    @GetMapping("/purchased/{studentId}")
    public List<PurchasedCourseResponse> getPurchasedCourses(@PathVariable int studentId) {
        return enrollmentService.getPurchasedCoursesByStudentId(studentId);
    }
    
    @GetMapping("/status/{courseId}/{studentId}")
    public ResponseEntity<Map<String, Boolean>> checkEnrollmentStatus(
            @PathVariable int courseId, @PathVariable int studentId) {
        boolean isEnrolled = enrollmentService.isStudentEnrolled(courseId, studentId);
        System.out.println(" course Id :"+courseId +" studentId :"+studentId+" status : "+isEnrolled);
        Map<String, Boolean> response = new HashMap();
        response.put("isEnrolled", isEnrolled);
        return ResponseEntity.ok(response);
    }
}