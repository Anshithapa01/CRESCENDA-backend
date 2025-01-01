package com.crescenda.backend.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crescenda.backend.event.WebSocketEvent;
import com.crescenda.backend.model.Message;
import com.crescenda.backend.request.MessageDeleteRequest;
import com.crescenda.backend.request.MessageEditRequest;
import com.crescenda.backend.request.MessageRequest;
import com.crescenda.backend.response.MessageResponse;
import com.crescenda.backend.service.MessageService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("notifications")
public class RealTimeChatController {


	private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    private MessageService messageService;
    
    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@RequestBody Message message) throws InterruptedException {
        System.out.println(message);
        return message;
    }

    @MessageMapping("/private-message")
    public MessageRequest privateMessage(@RequestBody MessageRequest message){
    	System.out.println(message.toString());
    	messageService.sendMessage(message.getChatId(), message.getContent(), message.getSenderRole(),message.getFileUrl(),message.getTimestamp());
        messagingTemplate.convertAndSendToUser(message.getRecipientUsername(),"/private",message);
        return message;
    }
  
    @MessageMapping("/edit-message")
    public void editMessage(@RequestBody MessageEditRequest editRequest) {
        MessageResponse updatedMessage = messageService.editMessage(editRequest.getChatId(), editRequest.getMessageId(), editRequest.getContent());
        messagingTemplate.convertAndSendToUser(
            editRequest.getRecipientUsername(),
            "/private",
            new WebSocketEvent("EDIT", updatedMessage)
        );
    }

    @MessageMapping("/delete-message")
    public void deleteMessage(@RequestBody MessageDeleteRequest deleteRequest) {
        messageService.deleteMessageById(deleteRequest.getMessageId());
        messagingTemplate.convertAndSendToUser(
            deleteRequest.getRecipientUsername(),
            "/private",
            new WebSocketEvent("DELETE", deleteRequest.getMessageId())
        );
    }
}


