package com.crescenda.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crescenda.backend.model.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    // Total review count
    @Query("SELECT COUNT(r) FROM Rating r")
    long findTotalReviewCount();

    // Count of reviews by specific rating
    @Query("SELECT COUNT(r) FROM Rating r WHERE r.rating = :rating")
    long countReviewsByRating(@Param("rating") int rating);
    
    List<Rating> findByCourse_CourseId(Long courseId);
    
    boolean existsByCourseCourseIdAndStudentStudentId(Long courseId, Long studentId);
    
    List<Rating> findByRootId(Long rootId);
    
    List<Rating> findByParentId(Long parentId); 


}