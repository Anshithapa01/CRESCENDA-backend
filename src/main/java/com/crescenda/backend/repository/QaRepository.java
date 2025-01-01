package com.crescenda.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crescenda.backend.model.QA;
import com.crescenda.backend.model.Task;

import java.util.List;
import java.util.Optional;


public interface QaRepository extends JpaRepository<QA, Long>{
	
	public QA findByEmailId(String emailid);
	
	List<QA> findByRole(String role);

	Optional<QA> findByQaUid(String qaExpertUid);
}
