package service;

import com.eoullim.domain.ChatMessage;
import com.eoullim.domain.ChatRoom;
import com.eoullim.repository.ChatRoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    private ChatRoom chatRoom;


}
