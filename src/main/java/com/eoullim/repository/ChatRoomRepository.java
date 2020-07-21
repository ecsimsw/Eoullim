package com.eoullim.repository;

import com.eoullim.domain.ChatRoom;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class ChatRoomRepository {

    private Map<Long, ChatRoom> chatRoomMap;

    @PostConstruct
    private void init(){ chatRoomMap = new LinkedHashMap<>(); }

    public List<ChatRoom> findAllRoom(){
        List chatRooms = new ArrayList<>(chatRoomMap.values());
        return chatRooms;
        // 이 부분 매번 리스트 만들면 성능 떨어질테니까, 다른 방법 고안.
    }

    public ChatRoom findRoomById(Long id){
        return chatRoomMap.get(id);
    }

    public ChatRoom save(String name){
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }
}
