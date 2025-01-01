package com.crescenda.backend.serviceImpl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.crescenda.backend.controller.WebSocketHandler;
import com.crescenda.backend.enumerator.SenderRole;
import com.crescenda.backend.model.Chat;
import com.crescenda.backend.model.Message;
import com.crescenda.backend.repository.ChatRepository;
import com.crescenda.backend.repository.MessageRepository;
import com.crescenda.backend.response.MessageResponse;
import com.crescenda.backend.service.MessageService;

import jakarta.transaction.Transactional;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;
    
    @Autowired
    private WebSocketHandler webSocketHandler;
    
    @Override
    public Message sendMessage(Long chatId, String content, String senderRole, String fileUrl,String timestamp) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found with id: " + chatId));

        Message message = new Message();
        message.setChat(chat);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setSenderRole(SenderRole.valueOf(senderRole.toUpperCase()));
        message.setFileUrl(fileUrl);

        chat.setLastMessage(content != "" ? content : "File");
        chat.setLastUpdated(LocalDateTime.now());
        chatRepository.save(chat);

        // Persist message
        message = messageRepository.save(message);

        // Broadcast message via WebSocket
//        String messagePayload = String.format("{\"chatId\":%d,\"content\":\"%s\",\"fileUrl\":\"%s\",\"senderRole\":\"%s\"}",
//                chatId, content, fileUrl, senderRole);

//        webSocketHandler.getSessions().values().forEach(session -> {
//            if (session.isOpen()) {
//                try {
//                    session.sendMessage(new TextMessage(messagePayload));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        return message;
    }


    @Override
    public List<MessageResponse> getChatMessages(Long chatId) {
        List<Message> messages = messageRepository.findByChatId(chatId);
        return messages.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public MessageResponse findMessageById(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found with id: " + messageId));
        return toResponse(message);
    }

    @Transactional
    @Override
    public void deleteMessageById(Long messageId) {
        // Retrieve the message
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found with id: " + messageId));
        System.out.println("Message: " + message);

        // Get associated chat
        Chat chat = message.getChat();

        // Delete the message
        messageRepository.deleteById(messageId);

        // Update chat's last message if needed
        List<Message> remainingMessages = messageRepository.findByChatId(chat.getId());
        if (!remainingMessages.isEmpty()) {
            remainingMessages.sort(Comparator.comparing(Message::getTimestamp).reversed());
            Message newLastMessage = remainingMessages.get(0);
            chat.setLastMessage(newLastMessage.getContent());
        } else {
            chat.setLastMessage(null);
        }
        chat.setLastUpdated(LocalDateTime.now());
        chatRepository.save(chat);

        System.out.println("Message deleted successfully with ID: " + messageId);
    }


    
    @Transactional
    @Override
    public MessageResponse editMessage(Long chatId,Long messageId, String newContent) {
    	Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found with id: " + chatId));
    	System.out.println("messageId :"+messageId);
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found with id: " + messageId));
        System.out.println("newContent :"+newContent);
        message.setContent(newContent);
        messageRepository.save(message);
        
        List<Message> chatMessages = chat.getMessages();
        chatMessages.sort(Comparator.comparing(Message::getTimestamp).reversed()); // Sort by timestamp in descending order
        Message lastMessage = chatMessages.isEmpty() ? null : chatMessages.get(0);
        System.out.println("This is the last message send by the user :"+lastMessage);
        if (lastMessage != null && lastMessage.getId().equals(messageId)) {
            // Update the last message content and last updated timestamp in Chat
            chat.setLastMessage(newContent != null && !newContent.isEmpty() ? newContent : "File");
            chatRepository.save(chat);
        }

        return toResponse(message);
    }


    private MessageResponse toResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getContent(),
                message.getTimestamp(),
                message.getSenderRole().toString(),
                message.getFileUrl(),
                message.getChat().getId()
        );
    }
}
