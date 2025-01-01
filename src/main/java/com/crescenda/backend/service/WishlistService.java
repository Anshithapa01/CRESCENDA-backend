package com.crescenda.backend.service;

import java.util.List;

import com.crescenda.backend.response.CourseResponse;

public interface WishlistService {

	void toggleWishlist(Long courseId, Long studentId);

	boolean isCourseInWishlist(Long courseId, Long studentId);

	List<CourseResponse> getWishlistByStudentId(Long studentId);

}
