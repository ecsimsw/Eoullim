package com.eoullim.form;

import com.eoullim.domain.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MessageForm {
    private String sender; // loginId
    private Long roomHash;
    private String message;
    private MessageType type;
}
