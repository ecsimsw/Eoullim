package com.eoullim.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
    private Long roomId;
    private String name;

    @OneToMany(mappedBy = "member")
    private List<Chat> members = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    //@JoinColumn(name="chat_message_id")
    private List<ChatMessage> chatMessages = new ArrayList<>();

    //private Set<WebSocketSession> memberSession = new HashSet<>();

    private static Long makeRoomId(){
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
        chatRoom.roomId = makeRoomId();
        chatRoom.name = name;
        return chatRoom;
    }

    /*
    public int handleMessage(WebSocketSession session, ChatMessage chatMessage, ObjectMapper objectMapper) throws IOException {
        int roomStatus = 0;  // -1 : End Room

        if(chatMessage.getType() == MessageType.ENTER){
            //memberSession.add(session);
            chatMessage.setMessage("Entered : "+chatMessage.getSender());
        }
        else if(chatMessage.getType() == MessageType.LEAVE) {
            //memberSession.remove(session);
            chatMessage.setMessage("Left : " + chatMessage.getSender());

            //if (memberSession.size() < 1) {roomStatus =-1;}
        }
        else{ chatMessage.setMessage(chatMessage.getSender() + " : " + chatMessage.getMessage());}

        send(chatMessage, objectMapper);
        return roomStatus;
    }

    private void send(ChatMessage chatMessage, ObjectMapper objectMapper) throws IOException {
        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(chatMessage.getMessage()));

        //for(WebSocketSession session : memberSession){
//            session.sendMessage(textMessage);
            // Send a WebSocket message: either TextMessage or BinaryMessage.
//        }
    }
    */
}
