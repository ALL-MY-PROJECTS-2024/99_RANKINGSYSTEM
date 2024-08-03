package com.creator.imageAndMusic.domain.service;

import com.creator.imageAndMusic.domain.dto.ChatRoomMusic;
import com.creator.imageAndMusic.domain.dto.TradingMusicDto;
import com.creator.imageAndMusic.domain.entity.MusicFileInfo;
import com.creator.imageAndMusic.domain.entity.MusicRanking;
import com.creator.imageAndMusic.domain.entity.TradingMusic;
import com.creator.imageAndMusic.domain.entity.User;
import com.creator.imageAndMusic.domain.repository.MusicFileInfoRepository;
import com.creator.imageAndMusic.domain.repository.MusicRankingRepository;
import com.creator.imageAndMusic.domain.repository.TradingMusicRepository;
import com.creator.imageAndMusic.domain.repository.UserRepository;
import com.creator.imageAndMusic.properties.SOCKET;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class TradingMusicServiceImpl {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TradingMusicRepository tradingMusicRepository;

    @Autowired
    private MusicFileInfoRepository musicFileInfoRepository;

    @Autowired
    private MusicRankingRepository musicRankingRepository;;

    //채팅관련 x
    private Map<String, ChatRoomMusic> chatRoomsMusic= new LinkedHashMap<>();

    private ObjectMapper objectMapper= new ObjectMapper();
    //-----------------------------------------------------------------
    // 음악 Trading
    //----------------------------------------------------------------
    @Transactional(rollbackFor=Exception.class)
    public Map<String,Object> requestTradingIMusic(TradingMusicDto dto){
        Map<String,Object> result = new HashMap();
        TradingMusic tadingMusic = new TradingMusic();
        User user = userRepository.findById(dto.getSeller()).get();
        MusicFileInfo musicFileInfo = musicFileInfoRepository.findById(dto.getFileid()).get();
        tadingMusic.setSeller(user);
        tadingMusic.setStartPrice(dto.getStartPrice());
        tadingMusic.setFileid(musicFileInfo);
        tadingMusic.setReqStartTime(LocalDateTime.now());
        tadingMusic.setReqTimeout(LocalDateTime.now().plusDays(14)); //14일뒤 제거 예정
        tadingMusic.setStartPrice(dto.getStartPrice());
        tadingMusic.setStatus("경매요청");

        MusicRanking musicRanking = musicRankingRepository.findByMusicFileInfo(musicFileInfo);
        if(musicRanking==null){
            log.info("랭킹등록이 되어있지 않아 경매요청할수 없습니다.");
            result.put("message","랭킹등록이 되어있지 않아 경매요청할수 없습니다.");
            result.put("status",false);
            return result;
        }
        if(musicRanking.getCount()<=1 || musicRanking.getIlikeit()<=1){
            log.info("조회수 1이상 / 좋아요 1 이상 이어야 경매등록할수 있습니다.");
            result.put("message","조회수 1이상 / 좋아요 1 이상 이어야 경매등록할수 있습니다.");
            result.put("status",false);
            return result;
        }
        if(tradingMusicRepository.existsBySellerAndFileid(user,musicFileInfo)){
            log.info("이미 경매요청 했습니다.");
            result.put("message","이미 경매요청 했습니다.\n매매>이미지 에서 확인하세요.");
            result.put("status",false);
            return result;
        }


        tradingMusicRepository.save(tadingMusic);
        log.info("경매등록 완료! 나의정보에서 확인해보세요.");
        result.put("message","경매등록 완료! 나의정보에서 확인해보세요.");
        result.put("status",true);
        return result;

    }

    @Transactional(rollbackFor=Exception.class)
    public List<TradingMusic> getMyTradingMusic(String seller){
        User user = userRepository.findById(seller).get();
        return  tradingMusicRepository.findAllBySeller(user);
    }
    @Transactional(rollbackFor=Exception.class)
    public List<TradingMusic> getMyAuctionedMusic(String buyer) {
        User user = userRepository.findById(buyer).get();
        return  tradingMusicRepository.findAllByBuyer(user);
    }

    @Transactional(rollbackFor=Exception.class)
    public TradingMusic getTradingMusic(Long tradingid){
        Optional<TradingMusic> tradingMusicOptional =    tradingMusicRepository.findById(tradingid);
        if(tradingMusicOptional.isEmpty())
            return null;
        return  tradingMusicOptional.get();
    }

    public List<TradingMusic> getAllTradingMusic() {
        return tradingMusicRepository.findAll();
    }

    public boolean acceptTradingMusic(TradingMusicDto dto) {
        Optional<TradingMusic> tradingMusicOptional =  tradingMusicRepository.findById(dto.getTradingid());
        if(tradingMusicOptional.isEmpty()){
            return false;
        }
        TradingMusic tradingMusic = tradingMusicOptional.get();
        tradingMusic.setAdminAccepted(true);
        tradingMusic.setAuctionStartTime(dto.getAuctionStartTime());
        tradingMusic.setStatus("경매승인");
        tradingMusicRepository.save(tradingMusic);
        return true;
    }

    public boolean cancelTradingMusic(Long tradingId) {
        Optional<TradingMusic> tradingMusicOptional =  tradingMusicRepository.findById(tradingId);
        if(tradingMusicOptional.isEmpty()){
            return false;
        }
        TradingMusic tradingMusic = tradingMusicOptional.get();
        tradingMusic.setAdminAccepted(false);
        tradingMusic.setAuctionStartTime(null);
        tradingMusic.setStatus("");
        tradingMusicRepository.save(tradingMusic);
        return true;
    }

    //채팅관련
    public List<ChatRoomMusic> findAllRoom() {
        return new ArrayList<>(chatRoomsMusic.values());
    }

    public ChatRoomMusic findRoomById(String roomId) {
        return chatRoomsMusic.get(roomId);
    }


    @Transactional(rollbackFor=Exception.class)
    public void createRoomMusic(String name,Long tradingid) {

        Map<String,Object> result = new LinkedHashMap<>();

        String randomId = UUID.randomUUID().toString();
        ChatRoomMusic chatRoom = ChatRoomMusic.builder()
                .roomId(randomId)
                .name(name)
                .tradingid(tradingid)
                .sessions(new HashMap<>())
                .build();
        chatRoomsMusic.put(randomId, chatRoom);

        System.out.println("createRoom!  : " + chatRoom);

        TradingMusic tradingMusic =  tradingMusicRepository.findById(tradingid).get();
        tradingMusic.setRoomId(chatRoom.getRoomId());
        tradingMusic.setMax(SOCKET.max);//정원 5명
        tradingMusic.setStatus("경매중");
        tradingMusicRepository.save(tradingMusic);
    }
    @Transactional(rollbackFor=Exception.class)
    public void joinChatMemberMusic(Long tradingid, String username) {
        TradingMusic tradingMusic =  tradingMusicRepository.findById(tradingid).get();

        if(tradingMusic.getCur() == null || tradingMusic.getCur()==0L){
            tradingMusic.setCur(1L);
            List<String> members = tradingMusic.getMembers();
            members.add(username);
            tradingMusicRepository.save(tradingMusic);
        }
        else if(tradingMusic.getCur()+1<=5)
        {
            tradingMusic.setCur(tradingMusic.getCur()+1);
            List<String> members = tradingMusic.getMembers();
            members.add(username);
            tradingMusicRepository.save(tradingMusic);
        }
    }

    @Transactional(rollbackFor=Exception.class)
    public void disconnectTradingIMageChatMusic(WebSocketSession session,String username) {
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

        for(String key : chatRoomsMusic.keySet()){
            ChatRoomMusic chatRoom =  chatRoomsMusic.get(key);
            Map<String,WebSocketSession> savedSessions = chatRoom.getSessions();
            Optional<TradingMusic> tradingMusicOptional =  tradingMusicRepository.findById(chatRoom.getTradingid());
            TradingMusic tradeMusic = tradingMusicOptional.get();

            for(WebSocketSession savedSession  :savedSessions.values()){
                if(savedSession.getId().equals(session.getId())){
                    if(tradeMusic.getCur()-1<=0){
                        tradeMusic.setCur(0L);
                        tradeMusic.setRoomId(null);
                    }else{
                        tradeMusic.setCur(tradeMusic.getCur()-1);
                    }

                    List<String> list =  tradeMusic.getMembers();
                    list.remove(username);
                    tradeMusic.setMembers(list);
                }
            }
        }




    }

    @Transactional(rollbackFor=Exception.class)
    public void updateTradingMusic(TradingMusic tradingMusic) {
        tradingMusicRepository.save(tradingMusic);
    }

    @Transactional(rollbackFor=Exception.class)
    public boolean removeTradingMusic(Long tradingid) {
//        Optional<TradingImage> tradingImageOptional =tradingImageRepository.findById(tradingid);
//
//        if(tradingImageOptional.isEmpty())
//            return false;
//        if(!tradingImageOptional.get().getSeller().equals(username)){
//            return false;
//        }
        tradingMusicRepository.deleteById(tradingid);
        return true;
    }

    @Transactional(rollbackFor=Exception.class)
    public void deleteChatMusic(Long tradingid) {

        for(String key : chatRoomsMusic.keySet()){
            ChatRoomMusic chatRoom = chatRoomsMusic.get(key);
            Long savedTradingId =  chatRoom.getTradingid();
            if(Objects.equals(savedTradingId, tradingid)){

                chatRoomsMusic.remove(key);

                Optional<TradingMusic> tradingMusicgOptional =  tradingMusicRepository.findById(tradingid);
                if(tradingMusicgOptional.isPresent()){
                    TradingMusic tradingMusic =tradingMusicgOptional.get();
                    tradingMusic.setCur(0L);
                    tradingMusic.setAuctionState("채팅방 제거");
                    tradingMusic.setRoomId(null);
                    tradingMusic.setStatus("경매중지");
                }
            }
        }
    }

    @Transactional(rollbackFor=Exception.class)
    public boolean commitTradingMusic(TradingMusicDto tradingMusicDto) {

        Optional<TradingMusic> tradingMusicOptional = tradingMusicRepository.findById(tradingMusicDto.getTradingid());
        if(tradingMusicOptional.isEmpty())
            return false;

        TradingMusic tradingMusic = tradingMusicOptional.get();

        Optional<User> buyerOptional =  userRepository.findById(tradingMusicDto.getBuyer());
        if(buyerOptional.isEmpty())
            return false;

        tradingMusic.setAuctionEndTime(LocalDateTime.now());
        tradingMusic.setBuyer(buyerOptional.get());
        tradingMusic.setPrice(tradingMusicDto.getPrice());
        tradingMusic.setStatus("낙찰완료");
        tradingMusicRepository.save(tradingMusic);

        return true;

    }

}
