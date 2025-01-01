package com.crescenda.backend.service;

import java.util.List;

import com.crescenda.backend.model.Rating;
import com.crescenda.backend.response.RatingResponse;

public interface RatingService {

	Rating createRating(Long courseId, Long studentId, Integer ratingValue, String reviewText);

	List<RatingResponse> getRatingsByCourseId(Long courseId);

	boolean hasStudentRatedCourse(Long courseId, Long studentId);

	List<RatingResponse> getCommentsByRootId(Long rootId);

	Rating addComment(Long courseId, Long studentId, Long parentId, Long rootId, String reviewText);

}
