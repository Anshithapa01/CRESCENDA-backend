package com.crescenda.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crescenda.backend.exception.UserException;
import com.crescenda.backend.response.ChatResponse;
import com.crescenda.backend.service.ChatService;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

	@Autowired
	private ChatService chatService ;

    @PostMapping("/{reqUser}")
    public ResponseEntity<ChatResponse> createChat(
            @RequestHeader long userId2,
            @PathVariable long reqUser) throws UserException {
    	System.out.println(reqUser+" "+userId2);
        ChatResponse chat = chatService.createChat(reqUser, userId2);
        return ResponseEntity.ok(chat);
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<String> deleteChat(
            @RequestHeader("userId") long reqUser,
            @PathVariable long chatId) throws UserException {
        chatService.deleteChat(chatId, reqUser);
        return ResponseEntity.ok("Chat deleted successfully.");
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ChatResponse>> findAllChatsByStudentId(@PathVariable long studentId) {
        List<ChatResponse> chats = chatService.findAllchatByStudendtId(studentId);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<List<ChatResponse>> findAllChatsByMentorId(@PathVariable long mentorId) {
        List<ChatResponse> chats = chatService.findAllchatByMentorId(mentorId);
        return ResponseEntity.ok(chats);
    }
}
