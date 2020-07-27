package com.eoullim.service;

import com.eoullim.domain.ChatMessage;
import com.eoullim.domain.Member;
import com.eoullim.domain.MessageType;
import com.eoullim.form.MessageForm;
import com.eoullim.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public ChatMessage createChatMessage(Member sender, MessageType messageType, String message, Long roomHash){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(sender);
        chatMessage.setType(messageType);
        chatMessage.setMessage(message);
        chatMessage.setRoomHash(roomHash);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    @Transactional
    public void save(ChatMessage chatMessage){
        chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> loadMessages(Long roomHash){
        return chatMessageRepository.getMessagesInRoom(roomHash);
    }

    @Transactional
    public void deleteAllInRoom(Long roomHash){
        chatMessageRepository.delteInRoom(roomHash);
    }
}
