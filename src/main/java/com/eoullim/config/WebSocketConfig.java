package com.eoullim.config;

import com.eoullim.handler.SocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

// webSocketHandlerRegistry에 sockethandler의 구현체를 등록한다.
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer{

    private final SocketHandler socketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler, "/eoullimChatServer");
    }
    // 클라이언트 접속시 "springServerIp:port/eoullimChatServer"로 접속.
    // 해당 소켓 핸들러를 registry 에 저장한다.
}