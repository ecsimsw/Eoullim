package com.eoullim.repository;

import com.eoullim.domain.ChatMessage;
import com.eoullim.domain.ChatRoom;
import com.eoullim.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryTest {

    @Autowired ChatMessageRepository chatMessageRepository;
    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void TestAll(){
        ChatRoom testChatRoom = chatRoomRepository.saveNewRoom("testChatRoom");

        Member testMember = new Member();
        memberRepository.save(testMember);

        ChatMessage msg1 = new ChatMessage();
        msg1.setChatRoom(testChatRoom);
        msg1.setMessage("입장입니다.");
        msg1.setSender(testMember);
        chatMessageRepository.save(msg1);

        ChatMessage msg2 = new ChatMessage();
        msg2.setChatRoom(testChatRoom);
        msg2.setMessage("test입니다.");
        msg2.setSender(testMember);
        chatMessageRepository.save(msg2);


        testChatRoom.getChatMessages().add(msg1);
        testChatRoom.getChatMessages().add(msg2);

        List<ChatMessage> list = new ArrayList<ChatMessage>();
        list.add(msg1);
        testChatRoom.setChatMessages(list);


        Assertions.assertThat(chatRoomRepository.findById(testChatRoom.getId()).getMembers().size()).isEqualTo(2);


    }

}
