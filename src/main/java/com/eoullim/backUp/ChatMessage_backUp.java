package com.eoullim.backUp;

import com.eoullim.domain.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChatMessage_backUp {
    private String sender;
    private Long roomId;
    private String message;

    private MessageType type;
}
