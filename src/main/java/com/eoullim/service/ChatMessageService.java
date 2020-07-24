package com.eoullim.service;

import com.eoullim.domain.ChatMessage;
import com.eoullim.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public void save(ChatMessage chatMessage){
        chatMessageRepository.save(chatMessage);
    }
}
