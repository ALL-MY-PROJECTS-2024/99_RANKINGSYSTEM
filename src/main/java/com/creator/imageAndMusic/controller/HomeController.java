package com.creator.imageAndMusic.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@Slf4j
public class HomeController {
    @GetMapping("/")
    public String home(){
        log.info("GET / ...");
        return "index";
    }




    @GetMapping("/login")
    public void login(){
        log.info("GET /login...");
    }



}
