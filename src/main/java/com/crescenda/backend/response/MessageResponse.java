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
public class MessageResponse {
    private Long id;
    private String content;
    private LocalDateTime timestamp;
    private String senderRole;
    private String fileUrl;
    private Long chatId;

}
