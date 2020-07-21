package com.eoullim.repository;

import com.eoullim.domain.ChatRoom;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class ChatRoomRepository {

    private Map<Long, ChatRoom> chatRoomMap;
    private List<ChatRoom> chatRooms; // for print chatRooms.

    @PostConstruct
    private void init(){
        chatRoomMap = new LinkedHashMap<>();
        chatRooms = new LinkedList<>();
    }

    public List<ChatRoom> findAllRoom(){
        return chatRooms;
    }

    public ChatRoom findRoomById(Long id){
        return chatRoomMap.get(id);
    }

    public ChatRoom save(String name){
        ChatRoom newChatRoom = ChatRoom.create(name);
        chatRoomMap.put(newChatRoom.getRoomId(), newChatRoom);
        chatRooms.add(newChatRoom);
        return newChatRoom;
    }
}
