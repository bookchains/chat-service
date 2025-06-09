package com.bookchain.chat.security;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        try {
            // ✅ UriComponentsBuilder 사용하여 토큰 추출
            UriComponents components = UriComponentsBuilder.fromUri(request.getURI()).build();
            String token = components.getQueryParams().getFirst("token");

            if (token == null || token.isBlank()) {
                log.warn("WebSocket 연결 시 토큰이 누락되었습니다.");
                return false;
            }

            // ✅ 토큰 검증
            if (!jwtUtil.validateToken(token)) {
                throw new JwtException("토큰이 유효하지 않음");
            }

            // ✅ 토큰에서 userId 추출하여 WebSocket 세션에 저장
            String address = jwtUtil.extractAddress(token);
            attributes.put("userId", address);
            return true;

        } catch (JwtException e) {
            log.warn("JWT 유효성 검증 실패: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("WebSocket 핸드셰이크 중 예외 발생: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // 필요 시 로깅 가능
        log.debug("WebSocket 핸드셰이크 완료");
    }
}