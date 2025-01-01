package com.crescenda.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crescenda.backend.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long>{

	public Student findByEmailId(String emailId);
	Optional<Student> findByStudentId(Integer studentId);
	public Student findByResetPasswordToken(String token);
}
