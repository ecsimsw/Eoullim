package com.eoullim.repository;

import com.eoullim.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepository {
    private final EntityManager em;

    public void save(ChatMessage chatMessage){
        em.persist(chatMessage);
    }

    public ChatMessage findById(Long id){
        return em.find(ChatMessage.class, id);
    }

    public List<ChatMessage> getMessagesInRoom(Long roomId){
        List<ChatMessage> messages =
                em.createQuery("Select m from ChatMessage m where m.roomId= :roomId", ChatMessage.class)
                        .setParameter("roomId", roomId)
                        .getResultList();
        return messages;
    }

    //public List<ChatMessage> findMessagesByRoomId(Long roomId){}

}
