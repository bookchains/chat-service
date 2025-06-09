package com.bookchain.chat.service;

import com.bookchain.chat.entity.TradeAgreement;
import com.bookchain.chat.repository.TradeAgreementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeAgreementService {

    private final TradeAgreementRepository repository;

    public void agreeToTrade(String roomId, String userId) {
        userId = userId.toLowerCase();
        log.info("[agreeToTrade] 요청 - roomId: {}, userId: {}", roomId, userId);

        try {
            if (!repository.existsByRoomIdAndUserId(roomId, userId)) {
                TradeAgreement agreement = TradeAgreement.builder()
                        .roomId(roomId)
                        .userId(userId)
                        .build();
                repository.save(agreement);
                log.info("[agreeToTrade] 저장 완료 - roomId: {}, userId: {}", roomId, userId);
            } else {
                log.info("[agreeToTrade] 이미 동의한 사용자 - roomId: {}, userId: {}", roomId, userId);
            }
        } catch (DataAccessException dae) {
            log.error("[agreeToTrade] DB 저장 오류 - roomId: {}, userId: {}, 메시지: {}", roomId, userId, dae.getMessage(), dae);
            throw dae;
        } catch (Exception e) {
            log.error("[agreeToTrade] 예외 발생 - roomId: {}, userId: {}, 메시지: {}", roomId, userId, e.getMessage(), e);
            throw e;
        }
    }

    public boolean isTradeAgreed(String roomId) {
        log.info("[isTradeAgreed] 거래 상태 확인 요청 - roomId: {}", roomId);
        try {
            List<TradeAgreement> agreements = repository.findByRoomId(roomId);
            log.info("[isTradeAgreed] 동의자 수: {}명 - roomId: {}", agreements.size(), roomId);
            return agreements.size() >= 2;
        } catch (DataAccessException dae) {
            log.error("[isTradeAgreed] DB 조회 오류 - roomId: {}, 메시지: {}", roomId, dae.getMessage(), dae);
            throw dae;
        } catch (Exception e) {
            log.error("[isTradeAgreed] 예외 발생 - roomId: {}, 메시지: {}", roomId, e.getMessage(), e);
            throw e;
        }
    }
}
