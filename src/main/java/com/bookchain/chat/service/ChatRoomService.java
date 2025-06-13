package com.bookchain.chat.service;

import com.bookchain.chat.entity.ChatRoom;
import com.bookchain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository repository;

    public ChatRoom createOrGetChatRoom(String buyerId, String sellerId, String tokenId) {
        buyerId = buyerId.toLowerCase();
        sellerId = sellerId.toLowerCase();

        log.info("[createOrGetChatRoom] 호출 - buyerId: {}, sellerId: {}, tokenId: {}", buyerId, sellerId, tokenId);

        if (buyerId == null || sellerId == null || tokenId == null) {
            log.error("[createOrGetChatRoom] buyerId, sellerId 또는 tokenId가 null입니다.");
            throw new IllegalArgumentException("buyerId, sellerId, tokenId는 null일 수 없습니다.");
        }

        try {
            Optional<ChatRoom> existing = repository.findByBuyerIdAndSellerIdAndTokenId(buyerId, sellerId, tokenId);

            if (existing.isPresent()) {
                log.info("[createOrGetChatRoom] 기존 채팅방 재사용 - roomId: {}", existing.get().getId());
                return existing.get();
            }

            String newRoomId = UUID.randomUUID().toString();
            ChatRoom newRoom = ChatRoom.builder()
                    .id(newRoomId)
                    .buyerId(buyerId)
                    .sellerId(sellerId)
                    .tokenId(tokenId)
                    .build();

            ChatRoom savedRoom = repository.save(newRoom);
            log.info("[createOrGetChatRoom] 새 채팅방 생성 - roomId: {}", savedRoom.getId());
            return savedRoom;

        } catch (DataAccessException dae) {
            log.error("[createOrGetChatRoom] DB 접근 오류 - buyerId: {}, sellerId: {}, tokenId: {}, 메시지: {}",
                    buyerId, sellerId, tokenId, dae.getMessage(), dae);
            throw dae;
        } catch (Exception e) {
            log.error("[createOrGetChatRoom] 알 수 없는 오류 - buyerId: {}, sellerId: {}, tokenId: {}, 메시지: {}",
                    buyerId, sellerId, tokenId, e.getMessage(), e);
            throw e;
        }
    }

    public List<ChatRoom> getMyChatRooms(String userId) {
        userId = userId.toLowerCase();
        log.info("[getMyChatRooms] 호출 - userId: {}", userId);

        if (userId == null) {
            log.error("[getMyChatRooms] userId가 null입니다.");
            throw new IllegalArgumentException("userId는 null일 수 없습니다.");
        }

        try {
            List<ChatRoom> rooms = repository.findByBuyerIdOrSellerId(userId, userId);
            log.info("[getMyChatRooms] {}개의 채팅방 조회됨", rooms.size());

            for (ChatRoom room : rooms) {
                log.debug("[getMyChatRooms] roomId: {}, buyerId: {}, sellerId: {}, tokenId: {}",
                        room.getId(), room.getBuyerId(), room.getSellerId(), room.getTokenId());
            }

            return rooms;

        } catch (DataAccessException dae) {
            log.error("[getMyChatRooms] DB 조회 실패 - userId: {}, 메시지: {}", userId, dae.getMessage(), dae);
            throw dae;
        } catch (Exception e) {
            log.error("[getMyChatRooms] 예외 발생 - userId: {}, 메시지: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    public void deleteRoom(String roomId) {
        log.warn("[deleteRoom] 호출 - roomId: {}", roomId);

        try {
            repository.deleteById(roomId);
            log.info("[deleteRoom] 채팅방 삭제 완료 - roomId: {}", roomId);
        } catch (DataAccessException dae) {
            log.error("[deleteRoom] DB 삭제 실패 - roomId: {}, 메시지: {}", roomId, dae.getMessage(), dae);
            throw dae;
        } catch (Exception e) {
            log.error("[deleteRoom] 예외 발생 - roomId: {}, 메시지: {}", roomId, e.getMessage(), e);
            throw e;
        }
    }
}