package com.eoullim.service;

import com.eoullim.domain.*;
import com.eoullim.repository.ChatMessageRepository;
import com.eoullim.repository.ChatRoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public void deleteMessagesInRoom(Long roomId){

    }

    @Transactional
    public ChatRoom createChatRoom(String name){
        return chatRoomRepository.saveNewRoom(name);
    }

    public List<ChatRoom> getAllChatRooms(){
        List<ChatRoom> rooms = chatRoomRepository.getAllRooms();
        Collections.reverse(rooms);
        return rooms;
    }

    public ChatRoom getChatRoomByHashId(Long hashId){
        return chatRoomRepository.findByRoomHash(hashId);
    }

    @Transactional
    public void deleteChatRoomByHashId(Long hashId){
        chatRoomRepository.deleteByRoomHash(hashId);
    }
}
