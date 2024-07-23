package com.creator.imageAndMusic.domain.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatRoom {
    private String roomId;
    private String name;
    private Long tradingid;
    private TradingImageDto tradingImageDto;
    private Map<String,WebSocketSession> sessions ;
}
