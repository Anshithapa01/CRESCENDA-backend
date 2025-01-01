package com.crescenda.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crescenda.backend.exception.UserException;
import com.crescenda.backend.response.CategoryResponse;
import com.crescenda.backend.response.CourseResponse;
import com.crescenda.backend.response.DraftResponse;
import com.crescenda.backend.response.MaterialResponse;
import com.crescenda.backend.response.MentorResponse;
import com.crescenda.backend.response.RatingResponse;
import com.crescenda.backend.response.SubCategoryResponse;
import com.crescenda.backend.service.CategoryService;
import com.crescenda.backend.service.CourseService;
import com.crescenda.backend.service.DraftService;
import com.crescenda.backend.service.MaterialService;
import com.crescenda.backend.service.MentorService;
import com.crescenda.backend.service.RatingService;
import com.crescenda.backend.service.SubCategoryService;

@RestController
public class HomeController {
	
	@Autowired
	private CourseService courseService;
	@Autowired
	private RatingService ratingService;
	@Autowired
    private DraftService draftService;
	@Autowired
	private MaterialService materialService;
	@Autowired
	private MentorService mentorService;
	@Autowired
    private CategoryService categoryService;
	@Autowired
    private SubCategoryService subCategoryService;
	
	@GetMapping("/courses")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<CourseResponse> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
	
	@GetMapping("/ratings/course/{courseId}")
    public ResponseEntity<List<RatingResponse>> getRatingsByCourseId(@PathVariable Long courseId) {
        List<RatingResponse> ratings = ratingService.getRatingsByCourseId(courseId);
        return ResponseEntity.ok(ratings);
    }
	
	@GetMapping("/admin/courses/draft/{draftId}/course-id")
    public ResponseEntity<Long> getCourseIdByDraftId(@PathVariable Long draftId) {
        try {
            Long courseId = courseService.getCourseIdByDraftId(draftId);
            if (courseId != null) {
                return ResponseEntity.ok(courseId);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
	@GetMapping("/mentor/draft/{draftId}")
    public ResponseEntity<DraftResponse> getDraftById(@PathVariable Long draftId) {
        DraftResponse draft = draftService.getDraftResponseById(draftId);
        return ResponseEntity.ok(draft);
    }
	
	@GetMapping("/ratings/comments/{ratingId}")
    public ResponseEntity<List<RatingResponse>> getCommentsByRatingId(@PathVariable Long ratingId) {
        List<RatingResponse> comments = ratingService.getCommentsByRootId(ratingId);
        return ResponseEntity.ok(comments);
    }
	
	@GetMapping("/mentor/draft/chapter/material/{chapterId}")
    public ResponseEntity<List<MaterialResponse>> getMaterialsByChapterId(@PathVariable Long chapterId) {
        List<MaterialResponse> materials = materialService.getMaterialsByChapterId(chapterId);
        return ResponseEntity.ok(materials);
    }
	
	@GetMapping("/mentor")
    public ResponseEntity<List<MentorResponse>> getAllMentors() {
        List<MentorResponse> mentors = mentorService.getAllMentors();
        return ResponseEntity.ok(mentors);
    }
	
	@GetMapping("/mentor/{mentorId}")
    public ResponseEntity<MentorResponse> getMentorById(@PathVariable Long mentorId) throws UserException {
        MentorResponse mentor = mentorService.getMentorById(mentorId);
        return ResponseEntity.ok(mentor);
    }

	@GetMapping("/admin/category")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
	
	@GetMapping("/admin/category/{categoryId}/subcategories")
	public ResponseEntity<List<SubCategoryResponse>> getSubCategories(@PathVariable Long categoryId) {
	    List<SubCategoryResponse> subCategories = subCategoryService.getSubCategoriesByCategoryId(categoryId);
	    return ResponseEntity.ok(subCategories); // Send the DTOs in the response
	}
	
	@GetMapping("/ratings/status")
    public ResponseEntity<Map<String, Boolean>> checkRatingStatus(
            @RequestParam Long courseId,
            @RequestParam Long studentId
    ) {
        boolean hasRated = ratingService.hasStudentRatedCourse(courseId, studentId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("hasRated", hasRated);
        return ResponseEntity.ok(response);
    }
}
