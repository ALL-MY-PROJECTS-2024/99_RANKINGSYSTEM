package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.domain.dto.BoardDto;
import com.creator.imageAndMusic.domain.dto.Criteria;
import com.creator.imageAndMusic.domain.dto.PageDto;
import com.creator.imageAndMusic.domain.entity.Board;
import com.creator.imageAndMusic.domain.entity.ImagesRanking;
import com.creator.imageAndMusic.domain.service.ImageRankingService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public void list(Model model)
    {
        log.info("GET /imageRanking/list... ");

        //파라미터 받기
        //유효성체크
        //서비스실행
        List<ImagesRanking> list =  imageRankingService.getAllImageRanking();

        model.addAttribute("list",list);
        //이동




    }

    @GetMapping("/read")
    public void readRanking(){}

}
