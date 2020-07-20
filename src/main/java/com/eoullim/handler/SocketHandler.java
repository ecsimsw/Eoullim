package com.eoullim.handler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
public class SocketHandler extends TextWebSocketHandler {

    private List<WebSocketSession> sessions = new LinkedList<>();


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("메세지 전송 = {} : {}",session,message.getPayload());
        for(WebSocketSession sess : sessions){
            TextMessage msg = new TextMessage(message.getPayload());
            sess.sendMessage(msg);
        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //socket connecting
        log.info("connected : "+session);

        super.afterConnectionEstablished(session);

        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //socket closing
        log.info("disConnected : "+session);

        super.afterConnectionClosed(session, status);

        sessions.remove(session);
    }
}
