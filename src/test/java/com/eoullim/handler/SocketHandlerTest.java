package com.eoullim.handler;

import com.eoullim.domain.*;
import com.eoullim.form.MessageForm;
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
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.sockjs.client.WebSocketClientSockJsSession;
import org.springframework.web.socket.sockjs.transport.session.WebSocketServerSockJsSession;

@SpringBootTest
@Component
@RunWith(SpringRunner.class)
public class SocketHandlerTest {

    @Autowired MemberService memberService;
    @Autowired ChatService chatService;
    @Autowired ChatRoomService chatRoomService;
    @Autowired ChatMessageService chatMessageService;

    private static Logger logger = LoggerFactory.getLogger(SocketHandlerTest.class);
    private static StringBuilder messages = new StringBuilder();
    @Test
    //@Rollback(false)
    @Transactional
    public void handleTextMessage() {
        logger.info("start");
        MessageForm messageForm_enter = new MessageForm();
        WebSocketSession session_enter = null;
        messageForm_enter.setMessage("ENTER_test");
        messageForm_enter.setRoomHash(20200726000858L);
        messageForm_enter.setType(MessageType.ENTER);
        messageForm_enter.setSender("a");

        Member sender_enter = memberService.getMemberByLoginId(messageForm_enter.getSender());
        logger.info("1");
        ChatMessage chatMessage_enter = new ChatMessage();
        chatMessage_enter.setSender(sender_enter);
        chatMessage_enter.setType(messageForm_enter.getType());
        chatMessage_enter.setMessage(messageForm_enter.getMessage());
        chatMessageService.save(chatMessage_enter);

        logger.info("2");
        Long roomHash_enter = messageForm_enter.getRoomHash();
        ChatRoom chatRoom_enter = chatRoomService.getChatRoomByHashId(roomHash_enter);
        chatRoom_enter.addChatMessage(chatMessage_enter);

        logger.info("3");
        if(chatMessage_enter.getType() == MessageType.ENTER){
            sender_enter.setWebSocketSession(session_enter);
            chatMessage_enter.setMessage("Entered : "+chatMessage_enter.getSender());
            chatService.createChat(sender_enter, chatRoom_enter);
        }

        logger.info(chatRoom_enter.getRoomHash().toString());
        logger.info(String.valueOf(chatRoom_enter.getChats().size()));
        for(Chat chat : chatRoom_enter.getChats()){
            logger.info("chat : "+chat.toString());
            logger.info("member : "+chat.getMember().getName());
            WebSocketSession session_get = chat.getMember().getWebSocketSession();
            logger.info("session : "+session_get);
            logger.info("msg : "+messageForm_enter.getMessage());
            messages.append(chat.getMember().getWebSocketSession()+"\n");
            //Send a WebSocket message: either TextMessage or BinaryMessage.
        }

        logger.info("CHAT");
        MessageForm messageForm = new MessageForm();
        WebSocketSession session = null;
        messageForm.setMessage("CHAT");
        messageForm.setRoomHash(20200726000858L);
        messageForm.setType(MessageType.CHAT);
        messageForm.setSender("a");

        Member sender = memberService.getMemberByLoginId(messageForm.getSender());
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
        if(chatMessage.getType() == MessageType.CHAT){
            chatMessage.setMessage(chatMessage.getSender()+" : "+chatMessage.getMessage());
        }

        logger.info(chatRoom.getRoomHash().toString());
        logger.info(String.valueOf(chatRoom.getChats().size()));
        for(Chat chat : chatRoom.getChats()){
            logger.info("chat : "+chat.toString());
            logger.info("member : "+chat.getMember().getName());
            WebSocketSession session_get = chat.getMember().getWebSocketSession();
            logger.info("session : "+session_get);
            logger.info("msg : "+messageForm.getMessage());
            messages.append(chat.getMember().getName()+"\n");
            //Send a WebSocket message: either TextMessage or BinaryMessage.
        }

        logger.info(messages.toString());
    }
}