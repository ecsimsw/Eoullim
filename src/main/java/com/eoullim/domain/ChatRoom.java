package com.eoullim.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Getter @Setter
public class ChatRoom {
    private Long roomId;
    private String name;
    private Set<WebSocketSession> memberSession = new HashSet<>();

    private static Long makeRoomId(){
        Long hash =0L;
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMddHHmmss", Locale.KOREA );
        hash += Long.parseLong(formatter.format ( currentTime ));
        long rand = (long)(Math.random()*100);
        hash += rand;
        return hash;
    }

    // 이게 왜 static 이지.
    // error: non-static method create(String) cannot be referenced from a static context
    public static ChatRoom create(String name){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = makeRoomId();
        chatRoom.name = name;
        return chatRoom;
    }

    public void handleMessage(WebSocketSession session, ChatMessage chatMessage, ObjectMapper objectMapper) throws IOException {
        if(chatMessage.getType() == MessageType.ENTER){
            memberSession.add(session);
            chatMessage.setMessage("Entered : "+chatMessage.getSender());
        }
        else if(chatMessage.getType() == MessageType.LEAVE){
            memberSession.remove(session);
            chatMessage.setMessage("Left : "+chatMessage.getSender());
        }
        else{
            chatMessage.setMessage(chatMessage.getSender() + " : " + chatMessage.getMessage());
        }
        send(chatMessage,objectMapper);
    }

    private void send(ChatMessage chatMessage, ObjectMapper objectMapper) throws IOException {
        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(chatMessage.getMessage()));

        for(WebSocketSession session : memberSession){
            session.sendMessage(textMessage);
        }
    }
}
