package com.bookchain.chat.controller;

import com.bookchain.chat.entity.ChatMessage;
import com.bookchain.chat.service.ChatService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat/messages")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatService chatService;

    @GetMapping("/{roomId}")
    public ResponseEntity<List<ChatMessage>> getMessages(
            @PathVariable String roomId,
            HttpServletRequest request) {

        String userId = (String) request.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(chatService.getMessages(roomId));
    }
}
