package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.config.auth.PrincipalDetails;
import com.creator.imageAndMusic.domain.dto.TradingImageDto;
import com.creator.imageAndMusic.domain.repository.TradingImageRepository;
import com.creator.imageAndMusic.domain.service.TradingImageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/trading")
public class TradingController {
   
   
    /*
        req : 사용자 경매 요청 ->
    */
    @Autowired
    TradingImageServiceImpl tradingImageService;

    @GetMapping("/req")
    public @ResponseBody ResponseEntity<String> req(@RequestParam("fildid") Long fileId, @AuthenticationPrincipal PrincipalDetails principalDetails){

        log.info("GET /trading/req " + fileId);
        TradingImageDto tradeImageDto = new TradingImageDto();
        tradeImageDto.setFileid(fileId);
        tradeImageDto.setSeller(principalDetails.getUsername());
        Map<String,Object> result =  tradingImageService.requestTradingImage(tradeImageDto);

        boolean status = (boolean) result.get("status");
        String message = (String) result.get("message");

        if(!status){
            return new ResponseEntity(message, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity(message, HttpStatus.OK);
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
    @GetMapping("/calendar/main")
    public void trading_calendar(){
        log.info("GET /trading/calendar/main");
    }

    @GetMapping("/calendar/add")
    public void trading_calendar_add(){
        log.info("GET /trading/calendar/add");
    }
    @GetMapping("/calendar/del")
    public void trading_calendar_del(){
        log.info("GET /trading/calendar/del");
    }
    @GetMapping("/calendar/update")
    public void trading_calendar_update(){
        log.info("GET /trading/calendar/update");
    }
}
