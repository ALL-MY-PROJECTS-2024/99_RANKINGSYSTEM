package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.config.auth.PrincipalDetails;
import com.creator.imageAndMusic.domain.dto.ChatRoom;
import com.creator.imageAndMusic.domain.dto.ChatRoomMusic;
import com.creator.imageAndMusic.domain.dto.TradingImageDto;
import com.creator.imageAndMusic.domain.dto.TradingMusicDto;
import com.creator.imageAndMusic.domain.entity.TradingImage;
import com.creator.imageAndMusic.domain.entity.TradingMusic;
import com.creator.imageAndMusic.domain.service.TradingImageServiceImpl;
import com.creator.imageAndMusic.domain.service.TradingMusicServiceImpl;
import com.creator.imageAndMusic.properties.SOCKET;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Controller
@Slf4j
@RequestMapping("/trading")
@Tag(name = "음악 경매", description = "음악 경매 처리관련 기능")
public class TradingMusicController {
   
    /*
        req : 사용자 경매 요청 ->
    */
    @Autowired
    TradingMusicServiceImpl tradingMusicService;


    @Operation(
            summary = "홈>매매",
            description = "음악 경매요청 처리 기능",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/music/req")
    public @ResponseBody ResponseEntity<String> req(@RequestParam("fildid") Long fileId, @RequestParam("startPrice") String startPrice,@AuthenticationPrincipal PrincipalDetails principalDetails){

        log.info("GET /trading/reqMusic " + fileId + " startPrice  :"+startPrice );
        TradingMusicDto tradeMusicDto = new TradingMusicDto();
        tradeMusicDto.setFileid(fileId);
        tradeMusicDto.setStartPrice(startPrice);
        tradeMusicDto.setSeller(principalDetails.getUsername());
        Map<String,Object> result =  tradingMusicService.requestTradingIMusic(tradeMusicDto);

        boolean status = (boolean) result.get("status");
        String message = (String) result.get("message");

        if(!status){
            return new ResponseEntity(message, HttpStatus.BAD_GATEWAY);
        }    return new ResponseEntity(message, HttpStatus.OK);
    }



    @PostMapping("/music/my")
    public void my(){
        log.info("GET /trading/music/my");
    }

    /*
    auction
    */
    @GetMapping("/music/auction/chat")
    public void auction_chat(){
        log.info("GET /trading/auction/chat");
    }



    /*
        image
    */


    /*
        music
    */


    /*
        calendar
    */
    @Operation(
            summary = "홈>매매>이벤트달력",
            description = "이벤트 달력 조회(음악 이벤트내역)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/music/calendar/main")
    public void trading_calendar(Model model){

        log.info("GET /trading/calendar/main");
        List<TradingMusic> listEntity =  tradingMusicService.getAllTradingMusic();

       List<TradingMusicDto> list = new ArrayList<>();

        listEntity.forEach(entity ->{
            TradingMusicDto dto = new TradingMusicDto();
            dto.setTradingid(entity.getTradingid());
            dto.setTitle("[IMAGE] " +  entity.getStatus());
            dto.setSeller((entity.getSeller()!=null)?entity.getSeller().getUsername():null);
            dto.setBuyer( (entity.getBuyer()!=null)?entity.getBuyer().getUsername():null);
            dto.setFileid(entity.getFileid().getFileid());
            dto.setAdminAccepted(entity.isAdminAccepted());
            dto.setAuctionStartTime(entity.getAuctionStartTime());
            dto.setAuctionEndTime(entity.getAuctionEndTime());
            dto.setPrice(entity.getPrice());
            dto.setPaymentState(entity.isPaymentState());
            dto.setStatus(entity.getStatus());

            //채팅방
            String roomId = entity.getRoomId();


            list.add(dto);
        });

        System.out.println(list);
        model.addAttribute("list",list);

    }

    @Operation(
            summary = "홈>매매>이벤트달력",
            description = "이벤트 달력 추가(음악)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/music/calendar/add")
    public void trading_calendar_add(){
        log.info("GET /trading/calendar/add");
    }
    @Operation(
            summary = "홈>매매>이벤트달력",
            description = "이벤트 달력 삭제(음악)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/music/calendar/del")
    public void trading_calendar_del(){
        log.info("GET /trading/calendar/del");
    }
    @Operation(
            summary = "홈>매매>이벤트달력",
            description = "이벤트 달력 수정(음악)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/music/calendar/update")
    public void trading_calendar_update(){
        log.info("GET /trading/calendar/update");
    }




    @Operation(
            summary = "홈>매매>음악",
            description = "음악 매매 내역 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/music/main")
    public void image_main(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        log.info("GET /trading/music/main..");
        List<TradingMusic> listEntity =  tradingMusicService.getAllTradingMusic();
        List<TradingMusicDto> list = new ArrayList();

        List<ChatRoomMusic> chatRooms =  tradingMusicService.findAllRoom();

        listEntity.forEach(entity ->{

                //재부팅시 초기화
                if(chatRooms.isEmpty()){
                    entity.setRoomId(null);
                    entity.setCur(0L);
                    entity.setMembers(new ArrayList<>());
                    tradingMusicService.updateTradingMusic(entity);
                }


                TradingMusicDto dto = new TradingMusicDto();
                dto.setTradingid(entity.getTradingid());
                dto.setTitle(entity.getFileid().getMusic().getTitle() );


                dto.setSeller((entity.getSeller()!=null)?entity.getSeller().getUsername():null);
                dto.setBuyer( (entity.getBuyer()!=null)?entity.getBuyer().getUsername():null);
                dto.setFileid(entity.getFileid().getFileid());
                dto.setFileDir(entity.getFileid().getDir());
                dto.setFilename(entity.getFileid().getFilename());
                dto.setAdminAccepted(entity.isAdminAccepted());
                dto.setAuctionStartTime(entity.getAuctionStartTime());
                dto.setAuctionEndTime(entity.getAuctionEndTime());
                dto.setPrice(entity.getPrice());
                dto.setPaymentState(entity.isPaymentState());
                dto.setCur(entity.getCur());
                dto.setStartPrice(entity.getStartPrice());
                dto.setStatus(entity.getStatus());
                dto.setAlbumImageName(entity.getFileid().getAlbumImageName());
                //채팅방
                dto.setRoomId(entity.getRoomId());
                dto.setMax(entity.getMax());
                dto.setMembers(entity.getMembers());

                //요청버튼 활성화여부
                List<String> members = entity.getMembers();

                if(members.isEmpty()){
                    dto.setReq(true);
                }
                else if(dto.getRoomId()==null){
                    dto.setReq(false);
                }
                else if(members.size()>=5){
                    dto.setReq(false);
                }
                else if(members.contains(principalDetails.getUserDto().getUsername())){
                    dto.setReq(false);
                }else{
                    dto.setReq(true);
                }



                //참가목록에 있다면 입장버튼 true
                for(String member : entity.getMembers()){
                    System.out.println(member + "==" +principalDetails.getUserDto().getUsername() +" > "+  member.equals(principalDetails.getUserDto().getUsername()));
                    if(member.equals(principalDetails.getUserDto().getUsername())&&dto.getRoomId()!=null ){
                        dto.setJoin(true);
                        break;
                    }
                }
                String role = principalDetails.getUserDto().getRole();
                if(role.equals("ROLE_ADMIN"))
                    dto.setJoin(true);
            list.add(dto);
        });

        System.out.println(list);
        model.addAttribute("list",list);
        model.addAttribute("username",principalDetails.getUserDto().getUsername());
        ;

    }
    //낙찰됨
    @Operation(
            summary = "홈>매매>음악",
            description = "-",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @PostMapping("/music/commit")
    public @ResponseBody ResponseEntity<String> commit(@ModelAttribute TradingMusicDto tradingMusicDto){
        log.info("GET /trading/image/commmit..." +tradingMusicDto);

        boolean isCommit =  tradingMusicService.commitTradingMusic(tradingMusicDto);
        if(!isCommit){
            return new ResponseEntity<>("fail",HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>("success",HttpStatus.OK);

    }

    //낙찰됨
    @Operation(
            summary = "홈>매매>음악",
            description = "음악 경매 정보 삭제",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/music/del")
    public String del(@RequestParam("tradingid") Long tradingid, RedirectAttributes attrs)
    {
        boolean isdel = tradingMusicService.removeTradingMusic(tradingid);
        attrs.addFlashAttribute("message","경매ID : " + tradingid + " 정보를 삭제하였습니다");
        return "redirect:/trading/music/main";
    }


    @Operation(
            summary = "홈>매매>음악",
            description = "음악 경매 승인",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/music/accept")
    public String trading_accept(TradingMusicDto dto){
      log.info("GET /trading/image/accept..." + dto.getTradingid());

      tradingMusicService.acceptTradingMusic(dto);

      return "redirect:/trading/music/main";
    }


    @Operation(
            summary = "홈>매매>음악",
            description = "음악 경매 채팅방 생성",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/music/chat/create")        //방을 만들었으면 해당 방으로 가야지.
    public String createRoom(@RequestParam("tradingid") Long tradingid, Model model) {
        log.info("POST /trading/music/chat/create... name : " + tradingid  );
        tradingMusicService.createRoomMusic("이미지 경매 채팅방",tradingid);
        //return "redirect:/trading/chat/room?roomId="+room.getRoomId()+"&username="+username;  //만든사람이 채팅방 1빠로 들어가게 됩니다
        return "redirect:/trading/music/main";
    }
    
    //채팅 관련

    @Operation(
            summary = "홈>매매>음악",
            description = "음악 경매 채팅 리스트 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/music/chat/list")
    public String chatList(Model model){
        List<ChatRoomMusic> roomList = tradingMusicService.findAllRoom();
        model.addAttribute("roomList",roomList);
        return "trading/music/chat/list";
    }



    private static Map<String,Object> joinMemberSession = new HashMap<String,Object>();
    @Operation(
            summary = "홈>매매>음악",
            description = "음악 경매 채팅방 진입",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/music/chat/enter")
    public String chat_room( @RequestParam("roomId") String roomId, @AuthenticationPrincipal PrincipalDetails principalDetails,Model model){
        ChatRoomMusic room = tradingMusicService.findRoomById(roomId);

        Long tradingid = room.getTradingid();
        TradingMusic tradingMusic = tradingMusicService.getTradingMusic(tradingid);


        String username = principalDetails.getUserDto().getUsername();
        Map<String, WebSocketSession> sessions =  room.getSessions();


        sessions.put(username,null);
        Set<String> users = sessions.keySet();
        System.out.println("/chat/enter... username : " + username);
        model.addAttribute("username",username);
        model.addAttribute("users", users);
        model.addAttribute("wspath", SOCKET.MUSIC_REQ_PATH);
        model.addAttribute("room",room);
        model.addAttribute("tradingMusic", tradingMusic);
        model.addAttribute("tradingid", tradingMusic.getTradingid());

        return "trading/chat/roomMusic";
    }


    @Operation(
            summary = "홈>매매>음악",
            description = "음악 경매 채팅방 참여 요청",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/music/chat/req")
    public String chat_req(@RequestParam("tradingid") Long tradingid,@AuthenticationPrincipal PrincipalDetails principalDetails)
    {
        String username = principalDetails.getUserDto().getUsername();
        tradingMusicService.joinChatMemberMusic(tradingid,username);

        return "redirect:/trading/music/main";
    }

    @Operation(
            summary = "홈>매매>음악",
            description = "음악 경매 채팅방 삭제",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/music/chat/del")
    public String chat_del(@RequestParam("tradingid") Long tradingid)
    {
        tradingMusicService.deleteChatMusic(tradingid);

        return "redirect:/trading/music/main";
    }



}
