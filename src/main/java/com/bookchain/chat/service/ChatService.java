package com.bookchain.chat.service;

import com.bookchain.chat.dto.ChatMessageDto;
import com.bookchain.chat.entity.ChatMessage;
import com.bookchain.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatMessageRepository repository;

    /**
     * 채팅 메시지를 저장
     */
    public void saveMessage(ChatMessageDto dto) {
        log.info("[saveMessage] 호출됨 - chatRoomId: {}, sender: {}, content: {}",
                dto.getChatRoomId(), dto.getSender(), dto.getContent());

        if (dto.getChatRoomId() == null || dto.getSender() == null || dto.getContent() == null) {
            log.error("[saveMessage] 필수값 누락 - chatRoomId: {}, sender: {}, content: {}",
                    dto.getChatRoomId(), dto.getSender(), dto.getContent());
            throw new IllegalArgumentException("채팅 메시지 저장 시 필수값(chatRoomId, sender, content)이 누락되었습니다.");
        }

        try {
            ChatMessage message = ChatMessage.builder()
                    .chatRoomId(dto.getChatRoomId())
                    .sender(dto.getSender())
                    .content(dto.getContent())
                    .timestamp(LocalDateTime.now())
                    .build();

            ChatMessage saved = repository.save(message);
            log.info("[saveMessage] 저장 성공 - messageId: {}, timestamp: {}", saved.getId(), saved.getTimestamp());

        } catch (DataAccessException dae) {
            log.error("[saveMessage] DB 저장 오류 - 메시지: {}", dae.getMessage(), dae);
            throw dae;
        } catch (Exception e) {
            log.error("[saveMessage] 예외 발생 - 메시지: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 특정 채팅방의 모든 메시지를 조회
     */
    public List<ChatMessage> getMessages(String chatRoomId) {
        log.info("[getMessages] 호출됨 - chatRoomId: {}", chatRoomId);

        if (chatRoomId == null || chatRoomId.isBlank()) {
            log.error("[getMessages] chatRoomId가 null 또는 빈 문자열입니다.");
            throw new IllegalArgumentException("chatRoomId는 null이거나 빈 문자열일 수 없습니다.");
        }

        try {
            List<ChatMessage> messages = repository.findByChatRoomIdOrderByTimestampAsc(chatRoomId);
            log.info("[getMessages] 총 {}개의 메시지 조회됨", messages.size());

            for (ChatMessage msg : messages) {
                log.debug("[getMessages] [{}] {}: {}", msg.getTimestamp(), msg.getSender(), msg.getContent());
            }

            return messages;

        } catch (DataAccessException dae) {
            log.error("[getMessages] DB 조회 오류 - chatRoomId: {}, 메시지: {}", chatRoomId, dae.getMessage(), dae);
            throw dae;
        } catch (Exception e) {
            log.error("[getMessages] 예외 발생 - chatRoomId: {}, 메시지: {}", chatRoomId, e.getMessage(), e);
            throw e;
        }
    }
}
