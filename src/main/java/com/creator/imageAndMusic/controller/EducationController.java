package com.creator.imageAndMusic.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/edu")
public class EducationController {

    @GetMapping("/stableDiffusion")
    public void t1(){
    }
    @GetMapping("/dalle")
    public void t2(){
    }
    @GetMapping("/mid")
    public void t3(){
    }
    @GetMapping("/genAi")
    public void t4(){
    }
    @GetMapping("/lecture")
    public void t5(){

    }
}
