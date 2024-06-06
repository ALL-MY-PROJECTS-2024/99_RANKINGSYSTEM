package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.domain.entity.ImagesFileInfo;
import com.creator.imageAndMusic.domain.service.AroundServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/around")
public class AroundController {


    @Autowired
    private AroundServiceImpl aroundService;
    @GetMapping("/popular")
    public void popular(){
        log.info("GET /around/popular");
    }

    @GetMapping("/group")
    public void group(){
        log.info("GET /around/group");
    }
    @GetMapping("/local")
    public void local(Model model){
        log.info("GET /around/local");

        List<ImagesFileInfo> list =  aroundService.getAllImages();
        for(ImagesFileInfo info : list){
            System.out.println(list);
        }
        model.addAttribute("list",list);
    }
    @GetMapping("/global")
    public void global(Model model){
        log.info("GET /around/global");
        List<ImagesFileInfo> list =  aroundService.getAllImages();
        for(ImagesFileInfo info : list){
            System.out.println(list);
        }
        model.addAttribute("list",list);

    }
}
