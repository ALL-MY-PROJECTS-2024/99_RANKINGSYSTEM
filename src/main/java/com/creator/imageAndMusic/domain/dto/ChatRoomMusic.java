package com.creator.imageAndMusic.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class ChatRoomMusic {
    private String roomId;
    private String name;
    private Long tradingid;
    private TradingMusicDto tradingMusicDto;
    private Map<String,WebSocketSession> sessions ;
}
