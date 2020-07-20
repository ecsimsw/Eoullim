package com.eoullim.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Getter @Setter
public class ChatRoom {
    private Long roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    private static Long makeRoomId(){
        Long hash =0L;
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMddHHmmss", Locale.KOREA );
        hash += Long.parseLong(formatter.format ( currentTime ));
        return hash;
    }

    public static ChatRoom create(String name){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = makeRoomId();
        chatRoom.name = name;
        return chatRoom;
    }

    public void handleMessage(WebSocketSession session, ChatMessage chatMessage, ObjectMapper objectMapper) throws IOException {
        if(chatMessage.getType() == MessageType.ENTER){
            sessions.add(session);
            chatMessage.setMessage("Entered : "+chatMessage.getSender());
        }
        else if(chatMessage.getType() == MessageType.LEAVE){
            sessions.remove(session);
            chatMessage.setMessage("Left : "+chatMessage.getSender());
        }
        else{
            chatMessage.setMessage(chatMessage.getSender() + " : " + chatMessage.getMessage());
        }
        send(chatMessage,objectMapper);
    }

    private void send(ChatMessage chatMessage, ObjectMapper objectMapper) throws IOException {
        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(chatMessage.getMessage()));

        for(WebSocketSession s : sessions){
            s.sendMessage(textMessage);
        }
    }
}