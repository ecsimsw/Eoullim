package com.eoullim.repository;

import com.eoullim.domain.Chat;
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

@RunWith(SpringRunner.class)
@SpringBootTest
public class EntityMappingTest {

    @Autowired ChatMessageRepository chatMessageRepository;
    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ChatRepository chatRepository;

    private static Logger logger = LoggerFactory.getLogger(EntityMappingTest.class);

    @Test
    @Transactional
    @Rollback(false)
    public void TestMsgInChatRoom(){
        Chat myChat = new Chat();
        ChatRoom testChatRoom = chatRoomRepository.saveNewRoom("testChatRoom");

        Member testMember = new Member();
        testMember.setName("jinhwan");
        memberRepository.save(testMember);

        myChat.setMember(testMember);
        myChat.setChatRoom(testChatRoom);
        chatRepository.save(myChat);

        ChatMessage msg1 = new ChatMessage();
        msg1.setMessage("입장입니다.");
        msg1.setSender(testMember);
        chatMessageRepository.save(msg1);

        ChatMessage msg2 = new ChatMessage();
        msg2.setMessage("test입니다.");
        msg2.setSender(testMember);
        chatMessageRepository.save(msg2);

        ChatRoom findRoom = chatRoomRepository.findById(testChatRoom.getId());

        Assertions.assertThat(findRoom).isEqualTo(testChatRoom);
        Assertions.assertThat(findRoom.getChats().size()).isEqualTo(testChatRoom.getChats().size());


    }

    @Test
    @Transactional
    @Rollback(false)
    public void TestMemberInChatRoom(){

        ChatRoom testChatRoom = chatRoomRepository.saveNewRoom("testChatRoom");

        Member memberJinhwan = new Member();
        memberJinhwan.setName("jinhwan");
        memberRepository.save(memberJinhwan);

        Member memberEcsimsw = new Member();
        memberEcsimsw.setName("Ecsimsw");
        memberRepository.save(memberEcsimsw);


        Chat jinhwanChat = new Chat();
        chatRepository.save(jinhwanChat);

        jinhwanChat.setMember(memberJinhwan);
        jinhwanChat.setChatRoom(testChatRoom);

        Chat ecsimswChat = new Chat();
        chatRepository.save(ecsimswChat);

        memberEcsimsw.addChat(ecsimswChat);
        memberEcsimsw.addChat(jinhwanChat);

        testChatRoom.addChat(jinhwanChat);
        testChatRoom.addChat(ecsimswChat);

        ChatRoom findRoom = chatRoomRepository.findById(testChatRoom.getId());

        Assertions.assertThat(findRoom.getChats().size()).isEqualTo(2);
        logger.info(String.valueOf(findRoom.getChats().size()));
        logger.info(String.valueOf(testChatRoom.getChats().size()));
    }
}
