package com.bookchain.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class CreateRoomRequest {
    private String sellerId;
    private String tokenId; // ✅ 책 고유 식별자
}