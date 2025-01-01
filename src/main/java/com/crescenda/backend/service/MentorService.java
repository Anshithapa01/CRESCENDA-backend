package com.crescenda.backend.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.crescenda.backend.exception.UserException;
import com.crescenda.backend.model.Mentor;
import com.crescenda.backend.response.MentorResponse;

public interface MentorService {

	Mentor findMentroById(long mentor_id) throws UserException;

	MentorResponse findMentorProfileByJwt(String jwt) throws UserException;

	Mentor registerMentor(Mentor mentor);

	Authentication authenticate(String username, String password);

	List<MentorResponse> getAllMentors();

	MentorResponse getMentorById(Long mentorId) throws UserException;

	void setBlockedStatus(Long id, boolean isBlocked);

	MentorResponse updateMentor(Long mentorId, Mentor updatedMentor);

	void resetPassword(String token, String password) throws UserException;

	void sendPasswordResetToken(String email) throws UserException;

}
