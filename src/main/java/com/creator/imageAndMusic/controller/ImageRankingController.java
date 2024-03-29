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
    public @ResponseBody ResponseEntity<String> addRanking(@RequestParam("fileid") Long fileid,Model model) throws Exception {
        log.info("imageRanking/add..fileid : " + fileid);

        boolean isAdded =  imageRankingService.addRankingImage(fileid);
        if(isAdded)
            return  new ResponseEntity("RANKING 등록 완료",HttpStatus.OK);
        else
            return  new ResponseEntity("RANKING 등록 실패",HttpStatus.BAD_GATEWAY);
    }


    @GetMapping("/list")
    public String list(@RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                       Model model,
                       HttpServletResponse response
    )
    {
        log.info("GET /imageRanking/list... ");

        //----------------
        //PageDto  Start
        //----------------
        Criteria criteria = null;
        if(pageNo==null) {
            //최초 /board/list 접근
            pageNo=1;
            criteria = new Criteria();  //pageno=1 , amount=10
        }
        else {
            criteria = new Criteria(pageNo,10); //페이지이동 요청 했을때
        }
        //파라미터 받기
        //유효성체크
        //서비스실행
        Map<String,Object> map =  imageRankingService.getAllImageRanking(criteria);

        PageDto pageDto = (PageDto) map.get("pageDto");
        List<ImagesRanking> list = (List<ImagesRanking>) map.get("list");
        List<ImagesRanking> rankingList = (List<ImagesRanking>) map.get("rankingList");
        int count = (int)map.get("count");


        model.addAttribute("pageNo",pageNo);
        model.addAttribute("pageDto",pageDto);
        model.addAttribute("list",list);
        model.addAttribute("rankingList",rankingList);

        //이동


        return "imageRanking/list";

    }

    @GetMapping("/read")
    public void readRanking(@RequestParam(name = "rankingId",defaultValue = "1") Long rankingId,Model model){
        log.info("GET /imageRanking/read..");


        ImagesRanking imagesRanking = imageRankingService.getImageRanking(rankingId);
        model.addAttribute("imagesRanking",imagesRanking);
        model.addAttribute("title","조회순");

        imageRankingService.count(rankingId);
    }

}
