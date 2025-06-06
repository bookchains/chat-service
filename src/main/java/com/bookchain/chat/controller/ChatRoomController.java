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

    /**
     * 구매자 → 판매자 채팅방 요청
     * 중복 방 생성 방지 (buyer-seller 조합 기준 정렬됨)
     */
    @PostMapping("/room")
    public ResponseEntity<ChatRoom> createOrGetRoom(
            @RequestBody CreateRoomRequest requestDto,
            HttpServletRequest request) {

        String buyerId = (String) request.getAttribute("userId");
        String sellerId = requestDto.getSellerId();
        String tokenId = requestDto.getTokenId(); // ✅ 추가

        if (buyerId == null || sellerId == null || buyerId.equals(sellerId)) {
            return ResponseEntity.badRequest().build();
        }

        ChatRoom room = chatRoomService.createOrGetChatRoom(buyerId, sellerId, tokenId);
        return ResponseEntity.ok(room);
    }


    /**
     * 로그인한 유저가 참여한 채팅방 전체 조회 (판매자/구매자 공통)
     */
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoom>> getMyChatRooms(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(chatRoomService.getMyChatRooms(userId));
    }

    /**
     * 채팅방 삭제 (soft delete)
     */
    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String roomId) {
        chatRoomService.deleteRoom(roomId);
        return ResponseEntity.ok().build();
    }
}
