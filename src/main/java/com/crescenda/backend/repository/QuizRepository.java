package com.crescenda.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crescenda.backend.model.Attempt;
import com.crescenda.backend.model.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByDraft_DraftId(Long draftId);

}