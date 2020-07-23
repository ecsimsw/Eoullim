package com.eoullim.repository;

import com.eoullim.domain.ChatRoom;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChatRoomRepositoryTest {

    @Autowired ChatRoomRepository chatRoomRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void saveAndFind() {
        Long savedId = chatRoomRepository.save("test");

       Assertions.assertThat(chatRoomRepository.findById(savedId).getName()).isEqualTo("test");
    }
}