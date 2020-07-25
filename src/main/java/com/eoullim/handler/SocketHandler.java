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

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {
    private final ChatRoomService chatRoomService;
    private final ObjectMapper objectMapper;
    private final MemberService memberService;
    private final ChatMessageService chatMessageService;
    private final ChatService chatService;

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
        log.info(messageForm.getSender());

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(sender);
        chatMessage.setType(messageForm.getType());
        chatMessage.setMessage(messageForm.getMessage());
        chatMessageService.save(chatMessage);

        Long roomHash = messageForm.getRoomHash();

        ChatRoom chatRoom = chatRoomService.getChatRoomByHashId(roomHash);

        if(chatMessage.getType() == MessageType.ENTER){
            sender.setWebSocketSession(session);
            chatMessage.setMessage("Entered : "+chatMessage.getSender().getName());

            chatService.createChat(sender, chatRoom);

        }
        else if(chatMessage.getType() == MessageType.LEAVE) {
            chatMessage.setMessage("Left : " + chatMessage.getSender());

            if (chatRoom.getChats().size() < 1) {roomStatus =-1;}
        }
        else{
            chatMessage.setMessage(chatMessage.getSender() + " : " + chatMessage.getMessage());
        }

        send(chatRoom, chatMessage);
        return roomStatus;
    }

    private void send(ChatRoom chatRoom, ChatMessage chatMessage) throws IOException {
        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(chatMessage.getMessage()));

        //log.info(chatRoom.getChats().toString());
        //log.info(chatMessage.getSender().toString());

        log.info(chatRoom.getRoomHash().toString());
        log.info(String.valueOf(chatRoom.getChats().size()));
        for(Chat chat : chatRoom.getChats()){
            log.info("chat : "+chat.toString());
            log.info("member : "+chat.getMember().getName());
            WebSocketSession session = chat.getMember().getWebSocketSession();
            log.info("session : "+session.toString());
            log.info("msg : "+textMessage);
            session.sendMessage(textMessage);

            //Send a WebSocket message: either TextMessage or BinaryMessage.
        }
    }
}
