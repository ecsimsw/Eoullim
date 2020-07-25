package com.eoullim.service;

import com.eoullim.domain.Chat;
import com.eoullim.domain.ChatRoom;
import com.eoullim.domain.Member;
import com.eoullim.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    @Transactional
    public void save(Chat chat){
        chatRepository.save(chat);
    }

    @Transactional
    public void createChat(Member member, ChatRoom chatRoom){
        Chat newChat = new Chat();

        member.addChat(newChat);
        chatRoom.addChat(newChat);

        chatRepository.save(newChat);
    }

    public String leftMember(ChatRoom chatRoom, Member member){
        String testLine ="left none";

        Iterator iterator = chatRoom.getChats().iterator();
        while(iterator.hasNext()){
            Chat c = (Chat)iterator.next();
            if(c.getMember().getName().equals(member.getName())){
                testLine = "left : "+member.getName();

                c.getMember().removeChat(c);
                chatRepository.delete(c);
                iterator.remove();
            }
        }

        return testLine;
    }
}
