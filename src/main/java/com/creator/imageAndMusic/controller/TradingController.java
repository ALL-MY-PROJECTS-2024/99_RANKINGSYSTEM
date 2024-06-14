package com.creator.imageAndMusic.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/trading")
public class TradingController {

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
