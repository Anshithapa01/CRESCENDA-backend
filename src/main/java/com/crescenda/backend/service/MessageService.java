package com.crescenda.backend.service;

import java.util.List;

import com.crescenda.backend.model.Message;
import com.crescenda.backend.response.MessageResponse;

public interface MessageService {
    Message sendMessage(Long chatId, String content, String senderRole,String fileUrl,String timestamp);
    List<MessageResponse> getChatMessages(Long chatId);
    MessageResponse findMessageById(Long messageId);
    void deleteMessageById(Long messageId);
	MessageResponse editMessage(Long chatId,Long messageId, String newContent);
}