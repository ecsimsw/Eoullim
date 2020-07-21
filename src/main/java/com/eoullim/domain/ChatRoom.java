package com.eoullim.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Getter @Setter
public class ChatRoom {
    private Long roomId;
    private String name;
    private Set<WebSocketSession> memberSession = new HashSet<>();

    // 왜 주입 못 받지..
    @Autowired
    private ObjectMapper objectMapper;

    private static Long makeRoomId(){
        Long hash =0L;
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMddHHmmss", Locale.KOREA );
        hash += Long.parseLong(formatter.format ( currentTime ));
        long rand = (long)(Math.random()*100);
        hash += rand;
        return hash;
    }

    // 이게 왜 static 이지. 아 자기 자신을 생성하는구나.
    public static ChatRoom create(String name){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = makeRoomId();
        chatRoom.name = name;
        return chatRoom;
    }

    public void handleMessage(WebSocketSession session, ChatMessage chatMessage) throws IOException {
        if(chatMessage.getType() == MessageType.ENTER){
            memberSession.add(session);
            chatMessage.setMessage("Entered : "+chatMessage.getSender());
        }
        else if(chatMessage.getType() == MessageType.LEAVE){
            memberSession.remove(session);
            chatMessage.setMessage("Left : "+chatMessage.getSender());
        }
        else{ chatMessage.setMessage(chatMessage.getSender() + " : " + chatMessage.getMessage()); }

        send(chatMessage);
    }

    private void send(ChatMessage chatMessage) throws IOException {
        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(chatMessage.getMessage()));

        for(WebSocketSession session : memberSession){
            session.sendMessage(textMessage);
        }
    }
}
