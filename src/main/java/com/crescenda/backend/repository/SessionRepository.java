package com.crescenda.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crescenda.backend.model.Sessions;
import com.crescenda.backend.model.Student;


public interface SessionRepository extends JpaRepository<Sessions, Long> {
    List<Sessions> findByStudent(Student student);

	Sessions findTopByStudentOrderByUpdatedAtDesc(Student student);
}