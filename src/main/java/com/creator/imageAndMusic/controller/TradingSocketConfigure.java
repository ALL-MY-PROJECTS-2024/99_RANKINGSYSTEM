package com.creator.imageAndMusic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class TradingSocketConfigure implements WebSocketConfigurer {
    private final TradingSocketHandler webSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
/*
그리고 setAllowedOrigins("*") 는 웹소켓 cors 정책으로 인해, 허용 도메인을 지정해줘야합니다.
현재 저희는 테스트이기때문에 * 와일드카드로 모든 도메인을 열어줍시다.(실제로 개발하실때에는 보안상의 위험으로 와일드카드는 쓰시면안됍니다.)
 */

        registry.addHandler(webSocketHandler, "/ws/chat").setAllowedOrigins("*");
//        registry.addHandler(webSocketHandler, "/wss/chat").setAllowedOrigins("https://test.wooriac.store");

    }
}