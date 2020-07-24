package com.eoullim.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="chat_message")
@Getter @Setter
public class ChatMessage {
    @Id @GeneratedValue
    @Column(name="chat_message_id")
    private Long id;

    @OneToOne
    private Member sender;

    private String message;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    private String date;
}
