package com.crescenda.backend.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageEditRequest {
	private Long chatId;
    private Long messageId;
    private String content;
    private String recipientUsername;
}
