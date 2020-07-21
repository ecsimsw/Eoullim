package com.eoullim.handler;

import com.eoullim.domain.ChatMessage;
import com.eoullim.domain.ChatRoom;
import com.eoullim.repository.ChatRoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {

    private final ChatRoomRepository chatRoomRepository;
    private final ObjectMapper objMapper;
    // jackson.ObjectMapper : mapping json to vo


    // TextMessage -> JSON {roomId : "id", type : "TYPE", sender : "sender"}
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("send = {} : {}",session,message.getPayload());
        // <<<< Only in Test >>>>

        String msg = message.getPayload();
        ChatMessage chatMessage = objMapper.readValue(msg, ChatMessage.class);
        ChatRoom chatRoom = chatRoomRepository.findRoomById(chatMessage.getRoomId());
        chatRoom.handleMessage(session,chatMessage,objMapper);
    }
}
