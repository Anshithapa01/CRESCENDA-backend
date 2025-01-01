package com.crescenda.backend.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {
    private Long id;
    private String lastMessage;
    private LocalDateTime lastUpdated;
    private MentorStudentResponse mentorStudent;
}
