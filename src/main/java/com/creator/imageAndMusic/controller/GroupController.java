package com.creator.imageAndMusic.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/group")
public class GroupController {

    @GetMapping("/main")
    public void main(){
        log.info("GET /group/main...");
    }
}
