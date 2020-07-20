package com.eoullim.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChatMessage {
    private String sender;
    private Long roomId;
    private String message;

    private MessageType type;
}
