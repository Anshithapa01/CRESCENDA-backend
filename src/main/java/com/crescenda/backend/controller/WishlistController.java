package com.crescenda.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crescenda.backend.request.WishlistRequest;
import com.crescenda.backend.response.CourseResponse;
import com.crescenda.backend.service.WishlistService;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
	
	 	@Autowired
	    private WishlistService wishlistService;
	    
	    @PostMapping("/toggle")
	    public ResponseEntity<String> toggleWishlist(@RequestBody WishlistRequest request) {
	        try {
	        	System.out.println("@RequestBody WishlistRequest request"+ request);
	            wishlistService.toggleWishlist(request.getCourseId(), request.getStudentId());
	            return ResponseEntity.ok("Wishlist updated successfully.");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                                 .body("Failed to update wishlist: " + e.getMessage());
	        }
	    }
	    
	    @GetMapping("/status/{courseId}/{studentId}")
	    public ResponseEntity<Map<String, Boolean>> getWishlistStatus(@PathVariable Long courseId, @PathVariable Long studentId) {
	        boolean isWishlisted = wishlistService.isCourseInWishlist(courseId, studentId);
	        Map<String, Boolean> response = new HashMap<>();
	        response.put("isWishlisted", isWishlisted);
	        return ResponseEntity.ok(response);
	    }
	    
	    @GetMapping("/{studentId}")
	    public ResponseEntity<List<CourseResponse>> getWishlist(@PathVariable Long studentId) {
	        List<CourseResponse> wishlistCourses = wishlistService.getWishlistByStudentId(studentId);
	        return ResponseEntity.ok(wishlistCourses);
	    }





}
