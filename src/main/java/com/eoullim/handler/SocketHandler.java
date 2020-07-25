package com.eoullim.handler;

import com.eoullim.domain.*;
import com.eoullim.form.MessageForm;
import com.eoullim.service.ChatMessageService;
import com.eoullim.service.ChatRoomService;
import com.eoullim.service.ChatService;
import com.eoullim.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {
    private final ChatRoomService chatRoomService;
    private final ObjectMapper objectMapper;
    private final MemberService memberService;
    private final ChatMessageService chatMessageService;
    private final ChatService chatService;

    private final StringBuilder sb = new StringBuilder();
    private final EntityManager em;

    private Map<String, WebSocketSession> sessionMap = new HashMap<>();
    private Set<String> memberInChatRoom = new HashSet<>();

    // TextMessage -> JSON {roomHash : "roomHashId", type : "TYPE", sender : "loginId", message:"content "}
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String msg = message.getPayload();
        MessageForm messageForm = objectMapper.readValue(msg, MessageForm.class);

        /*
        // mock
        session = null;
        messageForm.setMessage("ENTER_test");
        messageForm.setRoomHash(20200726000858L);
        messageForm.setType(MessageType.ENTER);
        messageForm.setSender("a");
         */

        handleMessageForm(session, messageForm);
    }

    @Transactional
    private int handleMessageForm(WebSocketSession session, MessageForm messageForm) throws IOException {
        int roomStatus = 0;  // -1 : End Room

        Member sender = memberService.getMemberByLoginId(messageForm.getSender());

        Long roomHash = messageForm.getRoomHash();
        ChatRoom chatRoom = chatRoomService.getChatRoomByHashId(roomHash);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(sender);
        chatMessage.setType(messageForm.getType());
        chatMessage.setMessage(messageForm.getMessage());
        chatMessage.setRoomHash(roomHash);


        //chatRoom.addChatMessage(chatMessage);

        if(chatMessage.getType() == MessageType.ENTER){
            String senderLoginId = sender.getLoginId();
            sessionMap.put(senderLoginId,session);
            //sender.setWebSocketSession(session);

            List<ChatMessage> loaded = chatMessageService.loadMessages(roomHash);
            for(ChatMessage message : loaded){
                log.info(message.getMessage());
                message.setMessage(message.getMessage());
                sendOne(chatRoom, message, senderLoginId);
            }
            chatMessage.setMessage("Entered : "+chatMessage.getSender().getName());
            chatService.createChat(sender, chatRoom);
        }
        else if(chatMessage.getType() == MessageType.LEAVE) {
            chatMessage.setMessage("Left : " + chatMessage.getSender().getName());
            Member leftOne = memberService.getMemberByLoginId(sender.getLoginId());
            sessionMap.remove(leftOne);

            String result = chatService.leftMember(chatRoom, leftOne);
            log.info(result);
            log.info(String.valueOf(chatRoom.getChats().size()));
            if (chatRoom.getChats().size() < 1) {
                //chatRoomService.deleteChatRoomByHashId(roomHash);
                roomStatus =-1;}
        }
        else{
            chatMessage.setMessage(chatMessage.getSender().getName() + " : " + chatMessage.getMessage());
        }

        chatMessageService.save(chatMessage);
        sendALL(chatRoom, chatMessage);
        return roomStatus;
    }

    private void sendALL(ChatRoom chatRoom, ChatMessage chatMessage) throws IOException {
        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(chatMessage.getMessage()));

        log.info("chatRoomHash : "+chatRoom.getRoomHash().toString());

        for(Chat chat : chatRoom.getChats()){
            //WebSocketSession session = chat.getMember().getWebSocketSession();
            memberInChatRoom.add(chat.getMember().getLoginId());
        }

        for(String memberLoginId : memberInChatRoom){
            WebSocketSession session = sessionMap.get(memberLoginId);
            session.sendMessage(textMessage); //send socket message: either TextMessage or BinaryMessage.
        }
    }

    private void sendOne(ChatRoom chatRoom, ChatMessage chatMessage, String loginId) throws IOException {
        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(chatMessage.getMessage()));
        WebSocketSession session = sessionMap.get(loginId);
        session.sendMessage(textMessage); //send socket message: either TextMessage or BinaryMessage.
    }

}
