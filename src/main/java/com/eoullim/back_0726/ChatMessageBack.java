package com.eoullim.back_0726;

import com.eoullim.domain.Member;
import com.eoullim.domain.MessageType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="chat_message")
@Getter
@Setter
public class ChatMessageBack {
    @Id
    @GeneratedValue
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