package com.crescenda.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crescenda.backend.model.Chat;
import com.crescenda.backend.model.MentorStudent;

public interface ChatRepository extends JpaRepository<Chat, Long>{

	Optional<Chat> findByMentorStudent(MentorStudent mentorStudent);

    List<Chat> findAllByMentorStudentStudentStudentIdAndStudentVisibleTrue(Long studentId);

    List<Chat> findAllByMentorStudentMentorMentorIdAndMentorVisibleTrue(Long mentorId);

    Chat findFirstByMentorStudentStudentStudentIdAndStudentVisibleTrue(Long studentId);

    Chat findFirstByMentorStudentMentorMentorIdAndMentorVisibleTrue(Long mentorId);
}
