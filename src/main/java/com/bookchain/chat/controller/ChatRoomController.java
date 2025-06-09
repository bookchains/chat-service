package com.bookchain.chat.controller;

import com.bookchain.chat.dto.CreateRoomRequest;
import com.bookchain.chat.entity.ChatRoom;
import com.bookchain.chat.service.ChatRoomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/room")
    public ResponseEntity<ChatRoom> createOrGetRoom(
            @RequestBody CreateRoomRequest requestDto,
            HttpServletRequest request) {

        String buyerId = ((String) request.getAttribute("userId")).toLowerCase();
        String sellerId = requestDto.getSellerId().toLowerCase();
        String tokenId = requestDto.getTokenId();

        System.out.println("buyerId: " + buyerId);
        System.out.println("sellerId: " + sellerId);
        System.out.println("tokenId: " + tokenId);

        if (buyerId == null || sellerId == null || buyerId.equals(sellerId)) {
            return ResponseEntity.badRequest().build();
        }

        ChatRoom room = chatRoomService.createOrGetChatRoom(buyerId, sellerId, tokenId);
        return ResponseEntity.ok(room);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoom>> getMyChatRooms(HttpServletRequest request) {
        String userId = ((String) request.getAttribute("userId")).toLowerCase();

        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(chatRoomService.getMyChatRooms(userId));
    }

    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(
            @PathVariable String roomId,
            HttpServletRequest request) {

        String userId = ((String) request.getAttribute("userId")).toLowerCase();

        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        chatRoomService.deleteRoom(roomId);
        return ResponseEntity.ok().build();
    }
}
