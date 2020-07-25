package com.eoullim.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="chat_message")
@Getter @Setter
public class ChatMessage {
    @Id @GeneratedValue
    @Column(name="chat_message_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    private Member sender;

    private String message;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    LocalDateTime date;

    private Long roomHash;
}
