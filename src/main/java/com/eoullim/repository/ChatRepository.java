package com.eoullim.repository;

import com.eoullim.domain.Chat;
import com.eoullim.domain.ChatMessage;
import com.eoullim.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ChatRepository {

    private final EntityManager em;

    public void save(Chat chat){
        em.persist(chat);
    }

    public void delete(Chat chat){
        Chat delete = em.find(Chat.class, chat.getId());
        //em.remove(delete);
        em.merge(delete);
    }

    public Chat find(Long id){
        return em.find(Chat.class, id);
    }


}
