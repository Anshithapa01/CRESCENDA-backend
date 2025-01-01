package com.crescenda.backend.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDeleteRequest {
    private Long messageId;
    private String recipientUsername;

}
