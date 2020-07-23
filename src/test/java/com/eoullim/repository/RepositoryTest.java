package com.eoullim.repository;

import com.eoullim.domain.ChatMessage;
import com.eoullim.domain.ChatRoom;
import com.eoullim.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryTest {

    @Autowired ChatMessageRepository chatMessageRepository;
    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired MemberRepository memberRepository;

    private static Logger logger = LoggerFactory.getLogger(RepositoryTest.class);

    @Test
    @Transactional
    @Rollback(false)
    public void TestAll(){
        ChatRoom testChatRoom = chatRoomRepository.saveNewRoom("testChatRoom");

        Member testMember = new Member();
        testMember.setName("jinhwan");
        memberRepository.save(testMember);

        ChatMessage msg1 = new ChatMessage();
        msg1.setMessage("입장입니다.");
        msg1.setSender(testMember);
        chatMessageRepository.save(msg1);
        testChatRoom.addChatMessage(msg1);

        ChatMessage msg2 = new ChatMessage();
        msg2.setMessage("test입니다.");
        msg2.setSender(testMember);
        chatMessageRepository.save(msg2);
        testChatRoom.addChatMessage(msg2);

        ChatRoom findRoom = chatRoomRepository.findById(testChatRoom.getId());

        Assertions.assertThat(findRoom).isEqualTo(testChatRoom);
        Assertions.assertThat(findRoom.getMembers().size()).isEqualTo(testChatRoom.getMembers().size());
        logger.info(String.valueOf(findRoom.getChatMessages().size()));
    }

}
