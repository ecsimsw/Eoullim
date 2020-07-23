package com.eoullim.repository;

import com.eoullim.Application;
import com.eoullim.domain.ChatMessage;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChatMessageRepositoryTest{

    @Autowired ChatMessageRepository chatMessageRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void getMessagesInRoom() {

        ChatMessage chat1 = new ChatMessage();
        ChatMessage chat2 = new ChatMessage();
        ChatMessage chat3 = new ChatMessage();
        ChatMessage chat4 = new ChatMessage();
        ChatMessage chat5 = new ChatMessage();

        chat1.setRoomId(1L);
        chat2.setRoomId(1L);
        chat3.setRoomId(1L);
        chat4.setRoomId(2L);
        chat5.setRoomId(3L);

        chatMessageRepository.save(chat1);
       chatMessageRepository.save(chat2);
        chatMessageRepository.save(chat3);
        chatMessageRepository.save(chat4);
        chatMessageRepository.save(chat5);

        Assertions.assertThat(chatMessageRepository.getMessagesInRoom(1L).size()).isEqualTo(3);
    }
}