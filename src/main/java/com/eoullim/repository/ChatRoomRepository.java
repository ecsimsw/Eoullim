package com.eoullim.repository;

import com.eoullim.domain.ChatMessage;
import com.eoullim.domain.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {

    private final EntityManager em;

    public Long save(String name){
        ChatRoom newChatRoom = ChatRoom.create(name);
        em.persist(newChatRoom);
        return newChatRoom.getId();
    }

    public ChatRoom findById(Long roomId){
        return em.find(ChatRoom.class, roomId);
    }

    public List<ChatRoom> getAllRooms(){
        List<ChatRoom> rooms =
                em.createQuery("Select m from ChatRoom m", ChatRoom.class)
                        .getResultList();
        return rooms;
    }

    /*
    private Map<Long, ChatRoom> chatRoomMap;
    private List<ChatRoom> chatRoomList; // for print chatRooms.

    @PostConstruct
    private void init(){
        chatRoomMap = new LinkedHashMap<>();
        chatRoomList = new LinkedList<>();
    }

    public List<ChatRoom> findAllRoom(){
        return chatRoomList;
    }

    public ChatRoom findRoomById(Long id){
        return chatRoomMap.get(id);
    }

    public ChatRoom save(String name){
        ChatRoom newChatRoom = ChatRoom.create(name);
        chatRoomMap.put(newChatRoom.getRoomId(), newChatRoom);
        chatRoomList.add(newChatRoom);
        return newChatRoom;
    }

    public void delete(Long roomId){
        ChatRoom deleteChatRoom = this.findRoomById(roomId);
        chatRoomList.remove(deleteChatRoom);
        chatRoomMap.remove(roomId);
    }
    */
}
