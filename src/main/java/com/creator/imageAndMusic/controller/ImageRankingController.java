package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.domain.service.ImageRankingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/imageRanking")
public class ImageRankingController {


    @ExceptionHandler
    private void exceptionHandler(Exception e){
        log.info("/imageRanking Exception.. ",e);
    }
    @Autowired
    private ImageRankingService imageRankingService;

    @GetMapping("/add")
    public @ResponseBody ResponseEntity<String> addRanking(@RequestParam("fileid") Long fileid) throws Exception {
        log.info("imageRanking/add..");

        boolean isAdded =  imageRankingService.addRankingImage(fileid);
        if(isAdded)
            return  new ResponseEntity("RANKING 등록 완료",HttpStatus.OK);
        else
            return  new ResponseEntity("RANKING 등록 실패",HttpStatus.BAD_GATEWAY);
    }

    @GetMapping("/list")
    public void listRanking(){}

    @GetMapping("/read")
    public void readRanking(){}

}
