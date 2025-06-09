package com.bookchain.chat.repository;

import com.bookchain.chat.entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    Optional<ChatRoom> findByBuyerIdAndSellerIdAndTokenId(String buyerId, String sellerId, String tokenId);
    List<ChatRoom> findByBuyerIdOrSellerId(String buyerId, String sellerId);
}