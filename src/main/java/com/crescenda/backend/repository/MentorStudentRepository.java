package com.crescenda.backend.repository;

import java.util.List;
import java.util.Optional;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crescenda.backend.model.Mentor;
import com.crescenda.backend.model.MentorStudent;
import com.crescenda.backend.model.Student;

public interface MentorStudentRepository extends JpaRepository<MentorStudent, Long> {
    List<MentorStudent> findByStudentStudentId(Long studentId); 
    List<MentorStudent> findByMentorMentorId(Long mentorId);
    boolean existsByStudentStudentIdAndMentorMentorId(Long studentId, Long mentorId);
    
    @Query("SELECT ms.mentor FROM MentorStudent ms WHERE ms.student.id = :studentId")
    List<Mentor> findMentorsByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT ms.mentor FROM MentorStudent ms WHERE ms.student.id = :studentId AND " +
           "(LOWER(ms.mentor.firstName) LIKE %:query% OR LOWER(ms.mentor.emailId) LIKE %:query%)")
    List<Mentor> searchMentorsByStudentIdAndQuery(@Param("studentId") Long studentId, @Param("query") String query);
    
    Optional<MentorStudent> findByStudentAndMentor(Student student1, Mentor mentorId);
    
    @Query("SELECT ms FROM MentorStudent ms WHERE ms.student.id = :studentId")
    List<MentorStudent> findAllByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT ms FROM MentorStudent ms " +
    	       "WHERE ms.student.id = :studentId " +
    	       "AND (LOWER(ms.mentor.firstName) LIKE %:query% " +
    	       "OR LOWER(ms.mentor.lastName) LIKE %:query%)")
    	List<MentorStudent> searchByStudentIdAndQuery(@Param("studentId") Long studentId, @Param("query") String query);

}