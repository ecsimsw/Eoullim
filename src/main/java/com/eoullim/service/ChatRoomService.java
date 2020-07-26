package com.eoullim.service;

import com.eoullim.domain.*;
import com.eoullim.repository.ChatMessageRepository;
import com.eoullim.repository.ChatRepository;
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
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;

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
        ChatRoom deleteOne = chatRoomRepository.findByRoomHash(hashId);
        for(Chat chat : deleteOne.getChats()){
            chat.setMember(null);
            chat.setChatRoom(null);
            chatRepository.delete(chat);
        }
        deleteOne.setChats(null);

        chatRoomRepository.deleteByRoomHash(hashId);
    }

    @Transactional
    public String exitMember(ChatRoom chatRoom, Member member){
        String testLine ="left none";

        Iterator iterator = chatRoom.getChats().iterator();
        while(iterator.hasNext()){
            Chat chat = (Chat)iterator.next();
            if(chat.getMember().getName().equals(member.getName())){
                testLine = "left : "+member.getName();

                chat.getMember().removeChat(chat);
                chat.setMember(null);
                //chat.getChatRoom().removeChat(chat);
                //chat.setChatRoom(null);

                chatRepository.delete(chat);
                iterator.remove();
            }
        }

        return testLine;
    }
}
