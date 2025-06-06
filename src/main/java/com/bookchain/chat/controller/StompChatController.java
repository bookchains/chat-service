package com.bookchain.chat.controller;

import com.bookchain.chat.dto.ChatMessageDto;
import com.bookchain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(
            @Header("simpSessionAttributes") Map<String, Object> attributes,
            @Payload ChatMessageDto message) {

        // sender를 세션에서 꺼내거나, 메시지 자체에 들어온 값 사용
        String sender = (String) attributes.get("userId");
        if (sender != null) {
            message.setSender(sender);
        }

        if (message.getChatRoomId() == null || message.getSender() == null || message.getContent() == null) {
            return; // 필수 항목이 없을 경우 무시
        }

        chatService.saveMessage(message);

        // 구독 중인 클라이언트에게 메시지 전송
        messagingTemplate.convertAndSend(
                "/topic/chat/" + message.getChatRoomId(),
                message
        );
    }
}
