package com.crescenda.backend.controller;

import com.crescenda.backend.model.Rating;
import com.crescenda.backend.response.RatingResponse;
import com.crescenda.backend.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    // Create a new rating
    @PostMapping
    public ResponseEntity<Rating> createRating(
            @RequestParam Long courseId,
            @RequestParam Long studentId,
            @RequestParam Integer rating,
            @RequestParam(required = false) String reviewText
    ) {
        Rating newRating = ratingService.createRating(courseId, studentId, rating, reviewText);
        return ResponseEntity.ok(newRating);
    }

    // Get all ratings by course ID
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<RatingResponse>> getRatingsByCourseId(@PathVariable Long courseId) {
        List<RatingResponse> ratings = ratingService.getRatingsByCourseId(courseId);
        return ResponseEntity.ok(ratings);
    }
    
    @GetMapping("/status")
    public ResponseEntity<Map<String, Boolean>> checkRatingStatus(
            @RequestParam Long courseId,
            @RequestParam Long studentId
    ) {
        boolean hasRated = ratingService.hasStudentRatedCourse(courseId, studentId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("hasRated", hasRated);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/comments")
    public ResponseEntity<Rating> addComment(
            @RequestParam Long courseId,
            @RequestParam Long studentId,
            @RequestParam(required = false) Long parentId,
            @RequestParam(required = false) Long rootId,
            @RequestParam String reviewText
    ) {
        Rating newComment = ratingService.addComment(courseId, studentId, parentId, rootId, reviewText);
        return ResponseEntity.ok(newComment);
    }

    @GetMapping("/comments/{ratingId}")
    public ResponseEntity<List<RatingResponse>> getCommentsByRatingId(@PathVariable Long ratingId) {
        List<RatingResponse> comments = ratingService.getCommentsByRootId(ratingId);
        return ResponseEntity.ok(comments);
    }

}