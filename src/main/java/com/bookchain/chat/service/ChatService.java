package com.bookchain.chat.service;

import com.bookchain.chat.dto.ChatMessageDto;
import com.bookchain.chat.entity.ChatMessage;
import com.bookchain.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository repository;

    public void saveMessage(ChatMessageDto dto) {
        ChatMessage message = ChatMessage.builder()
                .chatRoomId(dto.getChatRoomId())
                .sender(dto.getSender())
                .content(dto.getContent())
                .timestamp(LocalDateTime.now())
                .build();
        repository.save(message);
    }

    public List<ChatMessage> getMessages(String chatRoomId) {
        return repository.findByChatRoomIdOrderByTimestampAsc(chatRoomId);
    }
}
