package com.eoullim.backUp;

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