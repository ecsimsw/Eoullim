package com.eoullim.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


@Entity
@Table(name="chat_room")
@Getter @Setter
public class ChatRoom {

    @Id @GeneratedValue
    @Column(name ="chat_room_id")
    private Long id;
    private Long roomHash;
    private String name;

    // org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role -> Fetch 설정안했을 시
    @OneToMany(mappedBy = "chatRoom",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Chat> chats = new ArrayList<>();

    /* 일단 ChatRoom이랑 ChatMessage 매핑 없앰
    // org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role -> Fetch 설정안했을 시
    @OneToMany(cascade = CascadeType.ALL)  // 왜 Fetch 설정 안하면 에러가 나지
    @JoinColumn(name="chat_room_id")
    private List<ChatMessage> chatMessages = new ArrayList<>();

    public void addChatMessage(ChatMessage chatMessage){
        chatMessages.add(chatMessage);
    }

    public void clearChatMessage(){
        chatMessages.clear();
    }
  */

    public void addChat(Chat chat){
        chat.setChatRoom(this);
        chats.add(chat);
    }

    public void removeChat(Chat chat){
        chats.remove(chat);
    }

    private static Long makeRoomHash(){
        Long hash =0L;
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMddHHmmss", Locale.KOREA );
        hash += Long.parseLong(formatter.format ( currentTime ));
        long rand = (long)(Math.random()*100);
        hash += rand;
        return hash;
    }

    public static ChatRoom create(String name){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomHash = makeRoomHash();
        chatRoom.name = name;
        return chatRoom;
    }
}
