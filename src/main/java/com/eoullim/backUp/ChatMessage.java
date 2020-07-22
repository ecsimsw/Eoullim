package com.eoullim.backUp;

import com.eoullim.domain.Member;
import com.eoullim.domain.MessageType;
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
    @JoinColumn(name = "member_id")
    private Member sender;

    private String message;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    private Long roomId;
}
