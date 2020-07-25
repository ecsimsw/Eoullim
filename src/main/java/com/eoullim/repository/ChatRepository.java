package com.eoullim.repository;

import com.eoullim.domain.Chat;
import com.eoullim.domain.ChatMessage;
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

    public void delete(Chat chat){em.merge(chat);}

}
