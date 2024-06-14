package com.creator.imageAndMusic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
@Component
public class AuctionSocketHandler  extends TextWebSocketHandler {

    private static final ConcurrentHashMap<String, WebSocketSession> CLIENTS = new ConcurrentHashMap<String, WebSocketSession>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("연결 성공!...session : " + session +" | USERNAME  : " + session.getPrincipal().getName() );
        String username = session.getPrincipal().getName();

        CLIENTS.put(username.trim(), session);
        System.out.println(username+" 소켓 등록 전체소켓 : "+ CLIENTS.size());

        String id = session.getPrincipal().getName();  //메시지를 보낼 아이디
        TextMessage message = new TextMessage(id + " 님이 입장하셨습니다.");
        CLIENTS.entrySet().forEach( arg-> {

            try {
                arg.getValue().sendMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println(session.getPrincipal().getName() + " 연결종료");
        CLIENTS.remove( session.getPrincipal().getName());



    }

    public WebSocketSession findWebSocketSession(String username){

        for(String key : CLIENTS.keySet()){
            if(key.equals(username)){
                return CLIENTS.get(key);
            }
        }
        return null;
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Server Recv Message : " + message);
        String id = session.getPrincipal().getName();  //메시지를 보낼 아이디
        CLIENTS.entrySet().forEach( arg->{

            try {
                arg.getValue().sendMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

//            if(arg.getKey().equals(id)) //해당 아이디이면 전달
//            {
//                try {
//                    arg.getValue().sendMessage(message);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

        });


    }
}
