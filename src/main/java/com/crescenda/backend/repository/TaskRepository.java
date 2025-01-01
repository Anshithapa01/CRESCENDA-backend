package com.crescenda.backend.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crescenda.backend.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
	Optional<Task> findByDraftDraftId(Long draftId);
	List<Task> findByStatus(String status);
}