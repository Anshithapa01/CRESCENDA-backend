package com.crescenda.backend.service;

import java.util.List;

import com.crescenda.backend.exception.UserException;
import com.crescenda.backend.model.Chat;
import com.crescenda.backend.response.ChatResponse;

public interface ChatService {

	ChatResponse createChat(long reqStudent, long StudentId2) throws UserException;

	ChatResponse deleteChat(long chatId, long reqStudent) throws UserException;

	List<ChatResponse> findAllchatByStudendtId(long studentId);

	ChatResponse findChatByMentorId(long mentorId);

	ChatResponse findChatByStudentId(long studentId);

	List<ChatResponse> findAllchatByMentorId(long mentorId);
	
}
