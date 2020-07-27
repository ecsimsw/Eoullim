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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.persistence.EntityManager;
import javax.transaction.Transaction;
import java.io.IOException;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {
    private final ChatRoomService chatRoomService;
    private final ObjectMapper objectMapper;
    private final MemberService memberService;
    private final ChatMessageService chatMessageService;
    private final ChatService chatService;

    private Map<String, WebSocketSession> sessionMap = new HashMap<>();

    // TextMessage -> JSON {roomHash : "roomHashId", type : "TYPE", sender : "loginId", message:"content "}
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String msg = message.getPayload();
        MessageForm messageForm = objectMapper.readValue(msg, MessageForm.class);

        handleMessageForm(session, messageForm);
    }

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

            // 다른 방에 들어갔다가 나온건지, 아예 채팅방을 나갔다가 들어왔는지 구분해야함.
            if(!chatRoomService.isExist(chatRoom, sender.getId())){
                msgContent = "Entered : "+sender.getName();
                chatService.createChat(sender, chatRoom);
            }
            else{
                log.info("Already in this room. This must be error in going back");
            }
        }

        else if(msgType == MessageType.LEAVE) {    //방을 아예 나간 경우
            msgContent = "Left : " + sender.getName();
            Member leftOne = memberService.getMemberByLoginId(sender.getLoginId());
            sessionMap.remove(leftOne.getLoginId());

            chatRoom = chatRoomService.exitMember(chatRoom, leftOne);

            log.info("left chat count : "+chatRoom.getChats().size());
            log.info("left session count : "+sessionMap.size());

            if (chatRoom.getChats().size() < 1) {
                chatRoomService.deleteChatRoomByHashId(roomHash);
                chatMessageService.deleteAllInRoom(roomHash);
                log.info("delete room data and messages in room");
                roomStatus =-1;
                return roomStatus;
            }
        }

        // 브라우저에서 뒤로가기를 누르면 STEPOUT message를 전송한다. ( 잠깐 다른 방에 간 경우 )
        else if(msgType == MessageType.STEPOUT){
            Member leftOne = memberService.getMemberByLoginId(sender.getLoginId());
            sessionMap.remove(leftOne.getLoginId());
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
            WebSocketSession session = sessionMap.get(chat.getMember().getLoginId());

            if(session != null){
                session.sendMessage(textMessage);
                log.info("to -> "+ chat.getMember().getLoginId());
            }
            else{ log.info("==== web socket session is null ==="); }
        }
        log.info("sent successfully");
    }

    private void sendToOne(ChatMessage chatMessage, String loginId) throws IOException {
        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(chatMessage.getMessage()));
        WebSocketSession session = sessionMap.get(loginId);
        session.sendMessage(textMessage);
    }

}
