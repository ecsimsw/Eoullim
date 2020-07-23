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
    private Long roomHash;
    private String name;

    @OneToMany(mappedBy = "member")
    private List<Chat> members = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    //private Set<WebSocketSession> memberSession = new HashSet<>();

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
