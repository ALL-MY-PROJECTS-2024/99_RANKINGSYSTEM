package com.creator.imageAndMusic.controller;

import com.creator.imageAndMusic.domain.dto.ChatMessage;
import com.creator.imageAndMusic.domain.dto.ChatRoom;
import com.creator.imageAndMusic.domain.service.TradingImageServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
@Component
public class TradingSocketHandler extends TextWebSocketHandler {
    private  ObjectMapper objectMapper;
    public TradingSocketHandler(){
        objectMapper = new ObjectMapper();
    }
    @Autowired
    private TradingImageServiceImpl tradingImageServiceImpl;
    private static final ConcurrentHashMap<String, WebSocketSession> CLIENTS = new ConcurrentHashMap<String, WebSocketSession>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {


        System.out.println("연결 성공!...session : " + session +" | USERNAME  : " + session.getPrincipal().getName() );
        String username = session.getPrincipal().getName();

//        CLIENTS.put(username.trim(), session);
//        System.out.println(username+" 소켓 등록 전체소켓 : "+ CLIENTS.size());

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
        System.out.println(session);

        tradingImageServiceImpl.disconnectTradingIMageChat(session);

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
//        System.out.println("Server Recv Message : " + message);
//        String id = session.getPrincipal().getName();  //메시지를 보낼 아이디
//        CLIENTS.entrySet().forEach( arg->{
//
//            try {
//                arg.getValue().sendMessage(message);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//        });

        //--------------------------------------------------------

        String payload = message.getPayload();
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        ChatRoom room = tradingImageServiceImpl.findRoomById(chatMessage.getRoomId());

        Set<WebSocketSession> sessions=room.getSessions();      //방에 있는 현재 사용자 한명이 WebsocketSession
        System.out.println("handleTextMessage chatMessage" + chatMessage);
        System.out.println("handleTextMessage room " + room);
        System.out.println("handleTextMessage sessions " + sessions);
        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            //사용자가 방에 입장하면  Enter메세지를 보내도록 해놓음.  이건 새로운사용자가 socket 연결한 것이랑은 다름.
            //socket연결은 이 메세지 보내기전에 이미 되어있는 상태
            sessions.add(session);
            chatMessage.setMsg(chatMessage.getSender() + "님이 입장했습니다.");  //TALK일 경우 msg가 있을 거고, ENTER일 경우 메세지 없으니까 message set
            sendToEachSocket(sessions,new TextMessage(objectMapper.writeValueAsString(chatMessage)) );
        }else if (chatMessage.getType().equals(ChatMessage.MessageType.QUIT)) {
            sessions.remove(session);
            chatMessage.setMsg(chatMessage.getSender() + "님이 퇴장했습니다..");
            sendToEachSocket(sessions,new TextMessage(objectMapper.writeValueAsString(chatMessage)) );
        }else {
            sendToEachSocket(sessions,message ); //입장,퇴장 아닐 때는 클라이언트로부터 온 메세지 그대로 전달.
        }
        //--------------------------------------------------------
    }

    private  void sendToEachSocket(Set<WebSocketSession> sessions, TextMessage message){
        sessions.parallelStream().forEach( roomSession -> {
            try {
                roomSession.sendMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
