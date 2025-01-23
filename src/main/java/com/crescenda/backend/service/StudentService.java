package com.crescenda.backend.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.crescenda.backend.exception.UserException;
import com.crescenda.backend.model.Student;
import com.crescenda.backend.response.AuthResponse;
import com.crescenda.backend.response.StudentResponse;

public interface StudentService {

	public Student findStudentById(long student_id)throws UserException;
	
	public StudentResponse findUserProfileResponseByJwt(String jwt)throws UserException;
	
	public Student registerStudent(Student student)throws UserException;

	public Authentication authenticate(String username, String password);

	StudentResponse updateStudentById(long id, Student updatedStudent);

	StudentResponse getStudentById(long id);

	void setBlockedStatus(Long id, boolean isBlocked);

	List<StudentResponse> getAllStudents();

	StudentResponse mapToStudentResponse(Student student);

	void sendPasswordResetToken(String email) throws UserException;

	void resetPassword(String token, String newPassword) throws UserException;

	void createSession(String username, String token);

	Student verifyGoogleAccessToken(String accessToken) throws UserException;

	AuthResponse signupWithGoogle(String idToken) throws Exception;

	
	
}
