package com.crescenda.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crescenda.backend.model.Draft;

public interface DraftRepository extends JpaRepository<Draft, Long>{
	List<Draft> findByMentor_MentorId(Long mentorId);
	List<Draft> findByStatusOrderByAddedDateAsc(String status);
	List<Draft> findByStatusAndMentorMentorId(String status, Long mentorId);

}
