package com.eoullim.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.*;


@Entity
@Table(name="chat_room")
@Getter @Setter
public class ChatRoom {

    @Id @GeneratedValue
    @Column(name ="chat_room_id")
    private Long id;
    private String roomTitle;
    private Long roomHashId;
    private String writerLoginId;
    private String roomDescription;
    private Integer limitPerson;
    private String category;

    // org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role -> Fetch 설정안했을 시
    @OneToMany(mappedBy = "chatRoom",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Chat> chats = new ArrayList<>();

    public void addChat(Chat chat){
        chat.setChatRoom(this);
        chats.add(chat);
    }

    public void removeChat(Chat chat){
        chats.remove(chat);
    }

}
