package com.eoullim.service;

import com.eoullim.domain.ChatRoom;
import com.eoullim.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom createChatRoom(String name){
        return chatRoomRepository.save(name);
    }

    public ChatRoom getChatRoomById(Long roomId){
        return chatRoomRepository.findRoomById(roomId);
    }

    public List<ChatRoom> getAllchatRooms(){
        List<ChatRoom> rooms = chatRoomRepository.findAllRoom();
        Collections.reverse(rooms);
        return rooms;
    }


}
