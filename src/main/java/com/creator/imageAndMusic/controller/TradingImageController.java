package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.config.auth.PrincipalDetails;
import com.creator.imageAndMusic.domain.dto.ChatRoom;
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
@Tag(name = "경매", description = "경매 처리관련 기능")
public class TradingImageController {
   
    /*
        req : 사용자 경매 요청 ->
    */
    @Autowired
    TradingImageServiceImpl tradingImageService;
    @Autowired
    TradingMusicServiceImpl tradingMusicService;


    @Operation(
            summary = "홈>매매",
            description = "경매요청 처리 기능",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/req")
    public @ResponseBody ResponseEntity<String> req(@RequestParam("fildid") Long fileId, @RequestParam("startPrice") String startPrice,@AuthenticationPrincipal PrincipalDetails principalDetails){

        log.info("GET /trading/req " + fileId + " startPrice  :"+startPrice );
        TradingImageDto tradeImageDto = new TradingImageDto();
        tradeImageDto.setFileid(fileId);
        tradeImageDto.setStartPrice(startPrice);
        tradeImageDto.setSeller(principalDetails.getUsername());
        Map<String,Object> result =  tradingImageService.requestTradingImage(tradeImageDto);

        boolean status = (boolean) result.get("status");
        String message = (String) result.get("message");

        if(!status){
            return new ResponseEntity(message, HttpStatus.BAD_GATEWAY);
        }    return new ResponseEntity(message, HttpStatus.OK);
    }



    @PostMapping("/my")
    public void my(){
        log.info("GET /trading/my");
    }

    /*
    auction
    */


    @GetMapping("/auction/chat")
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
            description = "이벤트 달력 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/calendar/main")
    public void trading_calendar(Model model){

        log.info("GET /trading/calendar/main");
        List<TradingImage> listEntity =  tradingImageService.getAllTradingImages();

       List<TradingImageDto> list = new ArrayList<>();

        listEntity.forEach(entity ->{
            TradingImageDto dto = new TradingImageDto();
            dto.setTradingid(entity.getTradingid());
            dto.setTitle("[IMG]_"+entity.getTradingid()+"_"+  entity.getStatus());
            dto.setSeller((entity.getSeller()!=null)?entity.getSeller().getUsername():null);
            dto.setBuyer( (entity.getBuyer()!=null)?entity.getBuyer().getUsername():null);
            dto.setFileid(entity.getFileid().getFileid());
            dto.setAdminAccepted(entity.isAdminAccepted());
            dto.setAuctionStartTime(entity.getAuctionStartTime());
            dto.setAuctionEndTime(entity.getAuctionEndTime());
            dto.setStartPrice(entity.getStartPrice());
            dto.setPrice(entity.getPrice());
            dto.setPaymentState(entity.isPaymentState());
            dto.setStatus(entity.getStatus());

            //채팅방
            String roomId = entity.getRoomId();

            list.add(dto);
        });

        List<TradingMusic> musicList =  tradingMusicService.getAllTradingMusic();
        List<TradingMusicDto> list2 = new ArrayList<>();
        musicList.forEach(entity ->{
            TradingMusicDto dto = new TradingMusicDto();
            dto.setTradingid(entity.getTradingid());
            dto.setTitle("[MUSIC]_"+entity.getTradingid()+"_"+  entity.getStatus()) ;
            dto.setSeller((entity.getSeller()!=null)?entity.getSeller().getUsername():null);
            dto.setBuyer( (entity.getBuyer()!=null)?entity.getBuyer().getUsername():null);
            dto.setFileid(entity.getFileid().getFileid());
            dto.setAdminAccepted(entity.isAdminAccepted());
            dto.setAuctionStartTime(entity.getAuctionStartTime());
            dto.setAuctionEndTime(entity.getAuctionEndTime());
            dto.setPrice(entity.getPrice());
            dto.setStartPrice(entity.getStartPrice());
            dto.setPaymentState(entity.isPaymentState());
            dto.setStatus(entity.getStatus());

            //채팅방
            String roomId = entity.getRoomId();

            list2.add(dto);
        });


        System.out.println(list);
        model.addAttribute("list",list);
        model.addAttribute("list2",list2);

    }

    @Operation(
            summary = "홈>매매>이벤트달력",
            description = "이벤트 달력 추가",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/calendar/add")
    public void trading_calendar_add(){
        log.info("GET /trading/calendar/add");
    }

    @Operation(
            summary = "홈>매매>이벤트달력",
            description = "이벤트 달력 삭제",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/calendar/del")
    public void trading_calendar_del(){
        log.info("GET /trading/calendar/del");
    }

    @Operation(
            summary = "홈>매매>이벤트달력",
            description = "이벤트 달력 수정",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/calendar/update")
    public void trading_calendar_update(){
        log.info("GET /trading/calendar/update");
    }



    @Operation(
            summary = "홈>매매>이미지",
            description = "이미지 매매 내역 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/image/main")
    public void image_main(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        log.info("GET /trading/image/main..");
        List<TradingImage> listEntity =  tradingImageService.getAllTradingImages();
        List<TradingImageDto> list = new ArrayList();

        List<ChatRoom> chatRooms =  tradingImageService.findAllRoom();

        listEntity.forEach(entity ->{

                //재부팅시 초기화
                if(chatRooms.isEmpty()){
                    entity.setRoomId(null);
                    entity.setCur(0L);
                    entity.setMembers(new ArrayList<>());
                    tradingImageService.updateTradingImage(entity);
                }


                TradingImageDto dto = new TradingImageDto();
                dto.setTradingid(entity.getTradingid());
                dto.setTitle(entity.getFileid().getImages().getTitle() );


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
            summary = "홈>매매>이미지",
            description = "-",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @PostMapping("/image/commit")
    public @ResponseBody ResponseEntity<String> commit(@ModelAttribute TradingImageDto tradingImageDto){
        log.info("GET /trading/image/commmit..." +tradingImageDto);

        boolean isCommit =  tradingImageService.commitTradingImage(tradingImageDto);
        if(!isCommit){
            return new ResponseEntity<>("fail",HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>("success",HttpStatus.OK);

    }

    @Operation(
            summary = "홈>매매>이미지",
            description = "이미지 경매 정보 삭제",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/image/del")
    public String del(@RequestParam("tradingid") Long tradingid, RedirectAttributes attrs)
    {
        boolean isdel = tradingImageService.removeTradingImage(tradingid);
        attrs.addFlashAttribute("message","경매ID : " + tradingid + " 정보를 삭제하였습니다");
        return "redirect:/trading/image/main";
    }


    @Operation(
            summary = "홈>매매>이미지",
            description = "이미지 경매 승인",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/image/accept")
    public String trading_accept(TradingImageDto dto){
      log.info("GET /trading/image/accept..." + dto.getTradingid());

      tradingImageService.acceptTradingImages(dto);

      return "redirect:/trading/image/main";
    }


    @Operation(
            summary = "홈>매매>이미지",
            description = "경매 채팅방 생성",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/chat/create")        //방을 만들었으면 해당 방으로 가야지.
    public String createRoom(@RequestParam("tradingid") Long tradingid, Model model) {
        log.info("POST /trading/chat/create... name : " + tradingid  );
        tradingImageService.createRoom("이미지 경매 채팅방",tradingid);
        //return "redirect:/trading/chat/room?roomId="+room.getRoomId()+"&username="+username;  //만든사람이 채팅방 1빠로 들어가게 됩니다
        return "redirect:/trading/image/main";
    }
    
    //채팅 관련
    @Operation(
            summary = "홈>매매>이미지",
            description = "경매 채팅 리스트 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/chat/list")
    public String chatList(Model model){
        List<ChatRoom> roomList = tradingImageService.findAllRoom();
        model.addAttribute("roomList",roomList);
        return "trading/chat/list";
    }



    private static Map<String,Object> joinMemberSession = new HashMap<String,Object>();
    @Operation(
            summary = "홈>매매>이미지",
            description = "경매 채팅방 진입",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/chat/enter")
    public String chat_room( @RequestParam("roomId") String roomId, @AuthenticationPrincipal PrincipalDetails principalDetails,Model model){
        ChatRoom room = tradingImageService.findRoomById(roomId);

        Long tradingid = room.getTradingid();
        TradingImage tradingImage = tradingImageService.getTradingImage(tradingid);


        String username = principalDetails.getUserDto().getUsername();
        Map<String, WebSocketSession> sessions =  room.getSessions();


        sessions.put(username,null);
        Set<String> users = sessions.keySet();
        System.out.println("/chat/enter... username : " + username);
        model.addAttribute("username",username);
        model.addAttribute("users", users);
        model.addAttribute("wspath", SOCKET.REQ_PATH);
        model.addAttribute("room",room);
        model.addAttribute("tradingImage", tradingImage);
        model.addAttribute("tradingid", tradingImage.getTradingid());

        return "trading/chat/room";
    }

    @Operation(
            summary = "홈>매매>이미지",
            description = "경매 채팅방 참여 요청",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/chat/req")
    public String chat_req(@RequestParam("tradingid") Long tradingid,@AuthenticationPrincipal PrincipalDetails principalDetails)
    {
        String username = principalDetails.getUserDto().getUsername();
        tradingImageService.joinChatMember(tradingid,username);

        return "redirect:/trading/image/main";
    }

    @Operation(
            summary = "홈>매매>이미지",
            description = "경매 채팅방 삭제",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/chat/del")
    public String chat_del(@RequestParam("tradingid") Long tradingid)
    {
        tradingImageService.deleteChat(tradingid);

        return "redirect:/trading/image/main";
    }



}
