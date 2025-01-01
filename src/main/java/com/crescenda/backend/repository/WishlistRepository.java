package com.crescenda.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crescenda.backend.model.Course;
import com.crescenda.backend.model.Student;
import com.crescenda.backend.model.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
	Optional<Wishlist> findByStudentAndCourse(Student student, Course course);
	List<Wishlist> findByStudent(Student student);

}