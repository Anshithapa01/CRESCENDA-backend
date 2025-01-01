package com.crescenda.backend.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebSocketEvent {
    private String type; // "EDIT", "DELETE", etc.
    private Object payload;

    public WebSocketEvent(String type, Object payload) {
        this.type = type;
        this.payload = payload;
    }
}
