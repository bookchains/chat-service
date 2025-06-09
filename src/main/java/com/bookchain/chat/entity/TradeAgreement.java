// TradeAgreement.java
package com.bookchain.chat.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("trade_agreements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeAgreement {
    @Id
    private String id;

    private String roomId;
    private String userId; // 동의한 유저 지갑주소
}