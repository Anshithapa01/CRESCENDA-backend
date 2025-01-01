package com.crescenda.backend.repository;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crescenda.backend.model.Course;
import com.crescenda.backend.model.Wishlist;

public interface CourseRepository extends JpaRepository<Course, Long> {

	Optional<Course> findByDraft_DraftId(Long draftId);
	Optional<Course> findByCourseId(Integer courseId);
	@Query("SELECT COUNT(c) FROM Course c JOIN c.draft d WHERE d.mentor.mentorId = :mentorId AND c.isBlocked = FALSE")
    Long getActiveCoursesForMentor(@Param("mentorId") Long mentorId);
}