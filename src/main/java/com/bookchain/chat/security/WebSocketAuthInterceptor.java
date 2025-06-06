package com.bookchain.chat.security;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        String query = request.getURI().getQuery();
        if (query == null || !query.startsWith("token=")) {
            log.warn("WebSocket 연결 시 토큰이 누락되었습니다.");
            return false;
        }

        String token = query.substring("token=".length());

        try {
            if (!jwtUtil.validateToken(token)) {
                throw new JwtException("토큰이 유효하지 않음");
            }

            String address = jwtUtil.extractAddress(token);
            attributes.put("userId", address);
            return true;
        } catch (JwtException e) {
            log.warn("JWT 유효성 검증 실패: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // 생략 가능
    }
}
