package com.bookchain.chat.controller;

import com.bookchain.chat.service.TradeAgreementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat/trade")
@RequiredArgsConstructor
public class TradeAgreementController {

    private final TradeAgreementService tradeAgreementService;

    @PostMapping("/agree/{roomId}")
    public ResponseEntity<Void> agreeTrade(@PathVariable String roomId, HttpServletRequest request) {
        String userId = ((String) request.getAttribute("userId")).toLowerCase();
        if (userId == null) return ResponseEntity.badRequest().build();

        tradeAgreementService.agreeToTrade(roomId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status/{roomId}")
    public ResponseEntity<Boolean> checkTradeStatus(@PathVariable String roomId) {
        return ResponseEntity.ok(tradeAgreementService.isTradeAgreed(roomId));
    }
}