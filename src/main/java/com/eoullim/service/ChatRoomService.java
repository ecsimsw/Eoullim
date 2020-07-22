package com.eoullim.service;

import com.eoullim.domain.ChatMessage;
import com.eoullim.domain.ChatRoom;
import com.eoullim.repository.ChatRoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ObjectMapper objMapper;
    // 언제, 어떻게 빈으로 등록된거지.. -> JacksonAutoConfiguration!

    public ChatRoom createChatRoom(String name){
        return chatRoomRepository.save(name);
    }

    public ChatRoom getChatRoomById(Long roomId){
        return chatRoomRepository.findRoomById(roomId);
    }

    public List<ChatRoom> getAllChatRooms(){
        List<ChatRoom> rooms = chatRoomRepository.findAllRoom();
        Collections.reverse(rooms);
        return rooms;
    }

    public void sendMessage(WebSocketSession session, TextMessage message) throws IOException {
        String msg = message.getPayload();
        ChatMessage chatMessage = objMapper.readValue(msg, ChatMessage.class);

        Long roomId = chatMessage.getRoomId();
        ChatRoom chatRoom = chatRoomRepository.findRoomById(roomId);

        if(chatRoom.handleMessage(session,chatMessage, objMapper) == -1)
            deleteChatRoom(roomId);
    }

    public void deleteChatRoom(Long roomId){
        chatRoomRepository.delete(roomId);
    }
}
