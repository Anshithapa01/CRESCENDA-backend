package com.crescenda.backend.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageRequest {

	private Long chatId;
	private String content;
	private String senderRole;
	private String recipientUsername;
	private String senderUsername;
	private String timestamp;
	private String fileUrl;
}
