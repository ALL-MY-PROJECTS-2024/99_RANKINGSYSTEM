package com.creator.imageAndMusic.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/around")
public class AroundController {

    @GetMapping("/popular")
    public void popular(){
        log.info("GET /around/popular");
    }

    @GetMapping("/group")
    public void group(){
        log.info("GET /around/group");
    }
    @GetMapping("/local")
    public void local(){
        log.info("GET /around/local");
    }
    @GetMapping("/global")
    public void global(){
        log.info("GET /around/global");
    }
}
