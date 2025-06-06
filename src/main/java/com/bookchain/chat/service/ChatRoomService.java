package com.bookchain.chat.service;

import com.bookchain.chat.entity.ChatRoom;
import com.bookchain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository repository;

    /**
     * 동일 buyer-seller 조합 채팅방이 존재하면 재활용, 없으면 새로 생성
     */
    public ChatRoom createOrGetChatRoom(String buyerId, String sellerId, String tokenId) {
        if (buyerId == null || sellerId == null) {
            throw new IllegalArgumentException("buyerId 또는 sellerId가 null입니다.");
        }

        return repository.findByBuyerIdAndSellerId(buyerId, sellerId)
                .orElseGet(() -> repository.save(ChatRoom.builder()
                        .id(UUID.randomUUID().toString())
                        .buyerId(buyerId)
                        .sellerId(sellerId)
                        .tokenId(tokenId)
                        .build()));
    }

    /**
     * 로그인한 유저가 참여한 모든 채팅방 반환
     */
    public List<ChatRoom> getMyChatRooms(String userId) {
        return repository.findByBuyerIdOrSellerId(userId, userId);
    }

    /**
     * 하드 삭제 방식으로 채팅방 완전히 제거
     */
    public void deleteRoom(String roomId) {
        repository.deleteById(roomId);
    }
}
