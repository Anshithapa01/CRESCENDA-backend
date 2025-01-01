package com.crescenda.backend.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.crescenda.backend.exception.UserException;
import com.crescenda.backend.model.Admin;
import com.crescenda.backend.model.QA;
import com.crescenda.backend.request.QARequest;
import com.crescenda.backend.response.QAResponse;

public interface QaService {

	QA findQAProfileByJwt(String jwt) throws UserException;

	Authentication authenticateQA(String email, String rawPassword);

	List<QAResponse> getAllQAs();

	QAResponse getQAById(Long id);

	QA updateQA(Long id, QARequest qaDetails);

	QA saveQA(QARequest qaRequest) throws UserException;
	
	QAResponse convertToResponse(QA qa);

	List<QAResponse> getAllQAByRole(String role);

	void setBlockedStatus(Long id, boolean isBlocked);
	

}
