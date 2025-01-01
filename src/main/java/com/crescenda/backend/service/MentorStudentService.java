package com.crescenda.backend.service;

import java.util.List;

import com.crescenda.backend.response.MentorResponse;
import com.crescenda.backend.response.MentorStudentResponse;

public interface MentorStudentService {

	List<MentorStudentResponse> getAllMentorsByStudentId(Long studentId);

	List<MentorStudentResponse> searchMentorsByStudentIdAndQuery(Long studentId, String query);

}
