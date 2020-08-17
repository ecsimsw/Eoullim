package com.eoullim.service;

import com.eoullim.domain.*;
import com.eoullim.form.ChatRoomForm;
import com.eoullim.message.EcreateChatRoomMessage;
import com.eoullim.repository.ChatRepository;
import com.eoullim.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public EcreateChatRoomMessage createChatRoom(ChatRoomForm chatRoomForm, String category){
        ChatRoom newChatRoom = new ChatRoom();
        newChatRoom.setWriterLoginId(chatRoomForm.getWriterLoginId());
        newChatRoom.setRoomTitle(chatRoomForm.getRoomTitle());
        newChatRoom.setRoomHashId(chatRoomForm.getRoomHashId());
        newChatRoom.setRoomDescription(chatRoomForm.getRoomDescription());
        newChatRoom.setLimitPerson(chatRoomForm.getLimitPerson());
        newChatRoom.setCategory(category);

        chatRoomRepository.saveNewRoom(newChatRoom);

        return EcreateChatRoomMessage.success;
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
    public ChatRoom exitMember(ChatRoom chatRoom, Member member){

        chatRoom = chatRoomRepository.findByRoomHash(chatRoom.getRoomHashId());

        Iterator iterator = chatRoom.getChats().iterator();
        while(iterator.hasNext()){
            Chat chat = (Chat)iterator.next();
            if(chat.getMember().getName().equals(member.getName())){

                chat.getMember().removeChat(chat);
                chat.setMember(null);
                chat.setChatRoom(null);
                chatRepository.delete(chat);
                iterator.remove();
            }
        }
        return chatRoom;
    }

    public boolean isExist(ChatRoom chatRoom, Long memberId){
        for(Chat chat : chatRoom.getChats()){
            if(chat.getMember().getId() == memberId)
                return true;
        }

        return false;
    }
}
