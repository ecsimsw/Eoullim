package com.eoullim.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChatRoomRepositoryTest {

    @Autowired ChatRoomRepository chatRoomRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void saveAndFind() {
        Long savedRoomHash1 = chatRoomRepository.saveNewRoom("test").getRoomHash();
        Long savedRoomHash2 = chatRoomRepository.saveNewRoom("test").getRoomHash();
        Long savedRoomHash3 = chatRoomRepository.saveNewRoom("test").getRoomHash();

        Assertions.assertThat(chatRoomRepository.findByRoomHash(savedRoomHash1).getName()).isEqualTo("test");

        Assertions.assertThat(chatRoomRepository.getAllRooms().size()).isEqualTo(3);

        chatRoomRepository.deleteByRoomHash(savedRoomHash1);

        Assertions.assertThat(chatRoomRepository.getAllRooms().size()).isEqualTo(2);
    }
}