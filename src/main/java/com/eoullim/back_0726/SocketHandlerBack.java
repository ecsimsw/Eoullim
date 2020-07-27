package com.eoullim.back_0726;

import com.eoullim.domain.*;
import com.eoullim.form.MessageForm;
import com.eoullim.service.ChatMessageService;
import com.eoullim.service.ChatRoomService;
import com.eoullim.service.ChatService;
import com.eoullim.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketHandlerBack extends TextWebSocketHandler {
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
        MessageType msgType = messageForm.getType();
        String msgContent = messageForm.getMessage();
        Long roomHash = messageForm.getRoomHash();
        ChatRoom chatRoom = chatRoomService.getChatRoomByHashId(roomHash);

        // 일단 채팅방이랑 메시지 객체 연관 끊음
        // chatRoom.addChatMessage(chatMessage);

        if(msgType== MessageType.ENTER){

            // 현재 세션 맵에 (senderLoginId, session) 형태로 저장
            sessionMap.put(sender.getLoginId(),session);
            //sender.setWebSocketSession(session);

            // 이전 메시지 로드
            List<ChatMessage> loaded = chatMessageService.loadMessages(roomHash);
            for(ChatMessage message : loaded){
                message.setMessage(message.getMessage());
                sendToOne(message, sender.getLoginId());
            }

            /*
            // 같은 채팅 방 참여자 중복 제거
            Iterator iterator = chatRoom.getChats().iterator();
            while(iterator.hasNext()){
                Chat chatInRoom = (Chat)iterator.next();
                if(chatInRoom.getMember().getLoginId().equals(sender.getLoginId())){
                    iterator.remove();
                }
            }
            */

            msgContent = "Entered : "+sender.getName();
            chatService.createChat(sender, chatRoom);
        }
        else if(msgType == MessageType.LEAVE) {
            msgContent = "Left : " + sender.getName();
            Member leftOne = memberService.getMemberByLoginId(sender.getLoginId());
            sessionMap.remove(leftOne);

            chatRoomService.exitMember(chatRoom, leftOne);

            log.info(String.valueOf(chatRoom.getChats().size()));
            if (chatRoom.getChats().size() < 1) {
                chatRoomService.deleteChatRoomByHashId(roomHash);
                roomStatus =-1;
            }
        }
        else if(msgType == MessageType.CHAT){
            msgContent = sender.getName() + " : " + msgContent;
        }

        ChatMessage chatMessage = chatMessageService.createChatMessage(sender, msgType, msgContent, roomHash);
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

    private void sendToOne(ChatMessage chatMessage, String loginId) throws IOException {
        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(chatMessage.getMessage()));
        WebSocketSession session = sessionMap.get(loginId);
        session.sendMessage(textMessage); //send socket message: either TextMessage or BinaryMessage.
    }

}
