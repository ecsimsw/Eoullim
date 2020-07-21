package com.eoullim.handler;

import com.eoullim.domain.ChatMessage;
import com.eoullim.domain.ChatRoom;
import com.eoullim.repository.ChatRoomRepository;
import com.eoullim.service.ChatRoomService;
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
    private final ChatRoomService chatRoomService;

    // TextMessage -> JSON {roomId : "id", type : "TYPE", sender : "sender"}
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        chatRoomService.sendMessage(session, message);
    }
}
