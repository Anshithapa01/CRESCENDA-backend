package com.crescenda.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crescenda.backend.model.Attempt;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {
    List<Attempt> findByStudentStudentIdAndDraftDraftId(Long studentId, Long courseId);
    List<Attempt> findByDraft_DraftIdAndStudent_StudentId(Long draftId, Long studentId);

}