package com.creator.imageAndMusic.domain.service;


import com.creator.imageAndMusic.config.auth.PrincipalDetails;
import com.creator.imageAndMusic.domain.dto.ChatRoom;
import com.creator.imageAndMusic.domain.dto.TradingImageDto;
import com.creator.imageAndMusic.domain.entity.*;
import com.creator.imageAndMusic.domain.repository.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class TradingImageServiceImpl {
    @Autowired
    private TradingImageRepository tradingImageRepository;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImagesFileInfoRepository imagesFileInfoRepository;

    @Autowired
    private ImageRankingRepository imageRankingRepository;;



    //채팅 처리
    private  ObjectMapper objectMapper= new ObjectMapper();
    private Map<String, ChatRoom> chatRooms= new LinkedHashMap<>();

    @Transactional(rollbackFor=Exception.class)
    public Map<String,Object> requestTradingImage(TradingImageDto dto){
        Map<String,Object> result = new HashMap();
        TradingImage tradeImage = new TradingImage();
        User user = userRepository.findById(dto.getSeller()).get();
        ImagesFileInfo imagesFileInfo = imagesFileInfoRepository.findById(dto.getFileid()).get();
        tradeImage.setSeller(user);
        tradeImage.setStartPrice(dto.getStartPrice());
        tradeImage.setFileid(imagesFileInfo);
        tradeImage.setReqStartTime(LocalDateTime.now());
        tradeImage.setReqTimeout(LocalDateTime.now().plusDays(14)); //14일뒤 제거 예정
        tradeImage.setStartPrice(dto.getStartPrice());
        tradeImage.setStatus("경매요청");

        ImagesRanking imagesRanking = imageRankingRepository.findByImagesFileInfo(imagesFileInfo);
        if(imagesRanking==null){
            log.info("랭킹등록이 되어있지 않아 경매요청할수 없습니다.");
            result.put("message","랭킹등록이 되어있지 않아 경매요청할수 없습니다.");
            result.put("status",false);
            return result;
        }
        if(imagesRanking.getCount()<=1 || imagesRanking.getIlikeit()<=1){
            log.info("조회수 1이상 / 좋아요 1 이상 이어야 경매등록할수 있습니다.");
            result.put("message","조회수 1이상 / 좋아요 1 이상 이어야 경매등록할수 있습니다.");
            result.put("status",false);
            return result;
        }
        if(tradingImageRepository.existsBySellerAndFileid(user,imagesFileInfo)){
            log.info("이미 경매요청 했습니다.");
            result.put("message","이미 경매요청 했습니다.\n매매>이미지 에서 확인하세요.");
            result.put("status",false);
            return result;
        }


        tradingImageRepository.save(tradeImage);
        log.info("경매등록 완료! 나의정보에서 확인해보세요.");
        result.put("message","경매등록 완료! 나의정보에서 확인해보세요.");
        result.put("status",true);
        return result;

    }

    @Transactional(rollbackFor=Exception.class)
    public List<TradingImage> getMyTradingImage(String seller){
        User user = userRepository.findById(seller).get();
        return  tradingImageRepository.findAllBySeller(user);
    }
    @Transactional(rollbackFor=Exception.class)
    public List<TradingImage> getMyAuctionedImage(String buyer) {
        User user = userRepository.findById(buyer).get();
        return  tradingImageRepository.findAllByBuyer(user);
    }

    @Transactional(rollbackFor=Exception.class)
    public TradingImage getTradingImage(Long tradingid){
        Optional<TradingImage> tradingImageOptional =    tradingImageRepository.findById(tradingid);
        if(tradingImageOptional.isEmpty())
            return null;
        return  tradingImageOptional.get();
    }

    public List<TradingImage> getAllTradingImages() {
        return tradingImageRepository.findAll();
    }

    public boolean acceptTradingImages(TradingImageDto dto) {
        Optional<TradingImage> tradingImageOptional =  tradingImageRepository.findById(dto.getTradingid());
        if(tradingImageOptional.isEmpty()){
            return false;
        }
        TradingImage tradingImage = tradingImageOptional.get();
        tradingImage.setAdminAccepted(true);
        tradingImage.setAuctionStartTime(dto.getAuctionStartTime());
        tradingImage.setStatus("경매승인");
        tradingImageRepository.save(tradingImage);
        return true;
    }

    public boolean cancelTradingImages(Long tradingId) {
        Optional<TradingImage> tradingImageOptional =  tradingImageRepository.findById(tradingId);
        if(tradingImageOptional.isEmpty()){
            return false;
        }
        TradingImage tradingImage = tradingImageOptional.get();
        tradingImage.setAdminAccepted(false);
        tradingImage.setAuctionStartTime(null);
        tradingImage.setStatus("");
        tradingImageRepository.save(tradingImage);
        return true;
    }

    //채팅관련
    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }


    @Transactional(rollbackFor=Exception.class)

    public void createRoom(String name,Long tradingid) {

        Map<String,Object> result = new LinkedHashMap<>();

        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .tradingid(tradingid)
                .sessions(new HashMap<>())
                .build();
        chatRooms.put(randomId, chatRoom);

        System.out.println("createRoom!  : " + chatRoom);

        TradingImage tradingImage =  tradingImageRepository.findById(tradingid).get();
        tradingImage.setRoomId(chatRoom.getRoomId());
        tradingImage.setMax(5L);//정원 5명
        tradingImage.setStatus("경매중");
        tradingImageRepository.save(tradingImage);
    }
    @Transactional(rollbackFor=Exception.class)
    public void joinChatMember(Long tradingid, String username) {
        TradingImage tradingImage =  tradingImageRepository.findById(tradingid).get();

        if(tradingImage.getCur() == null || tradingImage.getCur()==0L){
            tradingImage.setCur(1L);
            List<String> members = tradingImage.getMembers();
            members.add(username);
            tradingImageRepository.save(tradingImage);
        }
        else if(tradingImage.getCur()+1<=5)
        {
            tradingImage.setCur(tradingImage.getCur()+1);
            List<String> members = tradingImage.getMembers();
            members.add(username);
            tradingImageRepository.save(tradingImage);
        }





    }

    @Transactional(rollbackFor=Exception.class)
    public void disconnectTradingIMageChat(WebSocketSession session,String username) {
        System.out.println("disconnectTradingIMageChat...");

        //Session이 비어이으면 모두 제거
//        for(String key : chatRooms.keySet()){
//            ChatRoom chatRoom =  chatRooms.get(key);
//            System.out.println("chatRoom : " + chatRoom);
//            //채팅방에 연결된 세션이 하나도 없다.(초기화)
//            Optional<TradingImage> tradingImageOptional =  tradingImageRepository.findById(chatRoom.getTradingid());
//            TradingImage tradeImage = tradingImageOptional.get();
//            if(chatRoom.getSessions().isEmpty()){
//                System.out.println("chatRoom.getSessions().isEmpty()! true");
//                tradeImage.setRoomId(null);
//                tradeImage.setCur(0L);
//                tradeImage.setMembers(null);
//                tradingImageRepository.save(tradeImage);
//                chatRooms.remove(chatRoom.getRoomId());
//            }
//        }

        for(String key : chatRooms.keySet()){
            ChatRoom chatRoom =  chatRooms.get(key);
            Map<String,WebSocketSession> savedSessions = chatRoom.getSessions();
            Optional<TradingImage> tradingImageOptional =  tradingImageRepository.findById(chatRoom.getTradingid());
            TradingImage tradeImage = tradingImageOptional.get();

            for(WebSocketSession savedSession  :savedSessions.values()){
                if(savedSession.getId().equals(session.getId())){
                    if(tradeImage.getCur()-1<=0){
                        tradeImage.setCur(0L);
                        tradeImage.setRoomId(null);
                    }else{
                        tradeImage.setCur(tradeImage.getCur()-1);
                    }

                    List<String> list =  tradeImage.getMembers();
                    list.remove(username);
                    tradeImage.setMembers(list);
                }
            }
        }




    }

    @Transactional(rollbackFor=Exception.class)

    public void updateTradingImage(TradingImage tradingImage) {
        tradingImageRepository.save(tradingImage);
    }

    @Transactional(rollbackFor=Exception.class)
    public boolean removeTradingImage(Long tradingid) {
//        Optional<TradingImage> tradingImageOptional =tradingImageRepository.findById(tradingid);
//
//        if(tradingImageOptional.isEmpty())
//            return false;
//        if(!tradingImageOptional.get().getSeller().equals(username)){
//            return false;
//        }
        tradingImageRepository.deleteById(tradingid);
        return true;
    }

    @Transactional(rollbackFor=Exception.class)
    public void deleteChat(Long tradingid) {

        for(String key : chatRooms.keySet()){
            ChatRoom chatRoom = chatRooms.get(key);
            Long savedTradingId =  chatRoom.getTradingid();
            if(Objects.equals(savedTradingId, tradingid)){

                chatRooms.remove(key);

                Optional<TradingImage> tradingImagOptional =  tradingImageRepository.findById(tradingid);
                if(tradingImagOptional.isPresent()){
                     TradingImage tradingImage =tradingImagOptional.get();
                    tradingImage.setCur(0L);
                    tradingImage.setAuctionState("채팅방 제거");
                    tradingImage.setRoomId(null);
                    tradingImage.setStatus("경매중지");
                }
            }
        }
    }

    @Transactional(rollbackFor=Exception.class)
    public boolean commitTradingImage(TradingImageDto tradingImageDto) {

        Optional<TradingImage> tradingImageOptional = tradingImageRepository.findById(tradingImageDto.getTradingid());
        if(tradingImageOptional.isEmpty())
            return false;

        TradingImage tradingImage = tradingImageOptional.get();

        Optional<User> buyerOptional =  userRepository.findById(tradingImageDto.getBuyer());
        if(buyerOptional.isEmpty())
            return false;

        tradingImage.setBuyer(buyerOptional.get());
        tradingImage.setPrice(tradingImageDto.getPrice());
        tradingImage.setStatus("낙찰완료");
        tradingImageRepository.save(tradingImage);

        return true;

    }


    public void updateTradingImageStatus(TradingImageDto tradingImageDto) {
        Optional<TradingImage> tradingImageOptional = tradingImageRepository.findById(tradingImageDto.getTradingid());
        if(tradingImageOptional.isEmpty())
            return ;
        TradingImage tradingImage = tradingImageOptional.get();
        tradingImage.setStatus(tradingImageDto.getStatus());
    }
}
