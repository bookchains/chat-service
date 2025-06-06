package com.bookchain.chat.repository;

import com.bookchain.chat.entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    Optional<ChatRoom> findByBuyerIdAndSellerId(String buyerId, String sellerId);
    List<ChatRoom> findByBuyerIdOrSellerId(String buyerId, String sellerId);

}
