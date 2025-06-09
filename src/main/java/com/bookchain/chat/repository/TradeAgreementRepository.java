package com.bookchain.chat.repository;

import com.bookchain.chat.entity.TradeAgreement;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TradeAgreementRepository extends MongoRepository<TradeAgreement, String> {
    List<TradeAgreement> findByRoomId(String roomId);
    boolean existsByRoomIdAndUserId(String roomId, String userId);
}