package com.eoullim.repository;

import com.eoullim.domain.Chat;
import com.eoullim.domain.ChatMessage;
import com.eoullim.domain.ChatRoom;
import com.eoullim.form.ChatRoomForm;
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

    public void save(ChatRoom chatRoom){
        if(chatRoom.getId()==null){
            em.persist(chatRoom);
        }
        else{
            em.merge(chatRoom);
        }
    }
    public void saveNewRoom(ChatRoom chatRoom){
        em.persist(chatRoom);
    }

    public ChatRoom findById(Long id){
        return em.find(ChatRoom.class, id);
    }

    public ChatRoom findByRoomHash(Long roomHash){
        List<ChatRoom> findRoom = em.createQuery("Select m from ChatRoom m where m.roomHash= :roomHash", ChatRoom.class)
                .setParameter("roomHash", roomHash)
                .getResultList();

        if(findRoom.size()==0) return null;
        else return findRoom.get(0);  // 동일한 roomHash는 없다고 가정.
    }

    public List<ChatRoom> getAllRooms(){
        List<ChatRoom> rooms =
                em.createQuery("Select m from ChatRoom m", ChatRoom.class)
                        .getResultList();
        return rooms;
    }

    public void deleteById(Long id){
        ChatRoom deleteOne = this.findById(id);
        em.remove(deleteOne);
    }

    public void deleteByRoomHash(Long roomHash){
        ChatRoom deleteOne = this.findByRoomHash(roomHash);
        em.remove(deleteOne);
    }
}
