package com.crescenda.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crescenda.backend.model.Message;
import com.crescenda.backend.response.MessageResponse;
import com.crescenda.backend.service.MessageService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

//    @PostMapping
//    public ResponseEntity<MessageResponse> sendMessage(
//            @RequestParam Long chatId,
//            @RequestParam String content,
//            @RequestParam String senderRole,
//            @RequestParam String fileUrl) {
//        Message message = messageService.sendMessage(chatId, content, senderRole,fileUrl);
//        return ResponseEntity.ok(new MessageResponse(
//                message.getId(),
//                message.getContent(),
//                message.getTimestamp(),
//                message.getSenderRole().toString(),
//                message.getFileUrl(),
//                message.getChat().getId()
//        ));
//    }

    @GetMapping("/{chatId}")
    public ResponseEntity<List<MessageResponse>> getChatMessages(@PathVariable Long chatId) {
        return ResponseEntity.ok(messageService.getChatMessages(chatId));
    }

    @GetMapping("/message/{messageId}")
    public ResponseEntity<MessageResponse> findMessageById(@PathVariable Long messageId) {
        return ResponseEntity.ok(messageService.findMessageById(messageId));
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessageById(@PathVariable Long messageId) {
        messageService.deleteMessageById(messageId);
        return ResponseEntity.noContent().build();
    }
}