package com.eoullim.handler;

import com.eoullim.Application;
import com.eoullim.domain.ChatMessage;
import com.eoullim.domain.ChatRoom;
import com.eoullim.domain.Member;
import com.eoullim.domain.MessageType;
import com.eoullim.form.MessageForm;
import com.eoullim.repository.EntityMappingTest;
import com.eoullim.service.ChatMessageService;
import com.eoullim.service.ChatRoomService;
import com.eoullim.service.ChatService;
import com.eoullim.service.MemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.sockjs.client.WebSocketClientSockJsSession;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SocketHandlerTest {

    @Autowired MemberService memberService;
    @Autowired ChatService chatService;
    @Autowired ChatRoomService chatRoomService;
    @Autowired ChatMessageService chatMessageService;

    private static Logger logger = LoggerFactory.getLogger(SocketHandlerTest.class);

    @Test
    @Rollback(false)
    @Transactional
    public void handleTextMessage() {
        logger.info("start");
        MessageForm messageForm = new MessageForm();
        WebSocketSession session = null;
        messageForm.setMessage("Enter");
        messageForm.setRoomHash(20200725014568L);
        messageForm.setType(MessageType.ENTER);
        messageForm.setSenderLoginId("a");

        Member sender = memberService.getMemberByLoginId(messageForm.getSenderLoginId());
        logger.info("1");
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(sender);
        chatMessage.setType(messageForm.getType());
        chatMessage.setMessage(messageForm.getMessage());
        chatMessageService.save(chatMessage);

        logger.info("2");
        Long roomHash = messageForm.getRoomHash();
        ChatRoom chatRoom = chatRoomService.getChatRoomByHashId(roomHash);
        chatRoom.addChatMessage(chatMessage);

        logger.info("3");
        if(chatMessage.getType() == MessageType.ENTER){
            sender.setWebSocketSession(session);
            chatMessage.setMessage("Entered : "+chatMessage.getSender());
            chatService.enterChatRoom(sender, chatRoom);
            logger.info("4");
        }
    }
}