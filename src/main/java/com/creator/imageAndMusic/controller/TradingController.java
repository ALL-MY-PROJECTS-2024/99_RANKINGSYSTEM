package com.creator.imageAndMusic.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/trading")
public class TradingController {
   
   
    /*
        req : 사용자 경매 요청 ->
    */
    @PostMapping("/req")
    public void req(){
        log.info("GET /trading/req");
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
