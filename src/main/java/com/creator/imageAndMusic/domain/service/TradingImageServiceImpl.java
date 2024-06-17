package com.creator.imageAndMusic.domain.service;


import com.creator.imageAndMusic.domain.dto.ChatRoom;
import com.creator.imageAndMusic.domain.dto.TradingImageDto;
import com.creator.imageAndMusic.domain.entity.ImagesFileInfo;
import com.creator.imageAndMusic.domain.entity.ImagesRanking;
import com.creator.imageAndMusic.domain.entity.TradingImage;
import com.creator.imageAndMusic.domain.entity.User;
import com.creator.imageAndMusic.domain.repository.ImageRankingRepository;
import com.creator.imageAndMusic.domain.repository.ImagesFileInfoRepository;
import com.creator.imageAndMusic.domain.repository.TradingImageRepository;

import com.creator.imageAndMusic.domain.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        tradeImage.setFileid(imagesFileInfo);
        tradeImage.setReqStartTime(LocalDateTime.now());
        tradeImage.setReqTimeout(LocalDateTime.now().plusDays(14)); //14일뒤 제거 예정

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

    public ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .sessions(new HashSet<>())
                .build();
        chatRooms.put(randomId, chatRoom);

        System.out.println("createRoom!  : " + chatRoom);
        return chatRoom;
    }

}
