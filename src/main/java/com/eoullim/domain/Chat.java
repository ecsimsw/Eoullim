package com.eoullim.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="chat")
public class Chat {
    @Id @GeneratedValue
    @Column(name ="chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="chat_room_id")
    private ChatRoom chatRoom;
}
