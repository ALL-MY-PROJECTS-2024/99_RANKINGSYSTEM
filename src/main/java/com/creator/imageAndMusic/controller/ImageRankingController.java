package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.config.auth.PrincipalDetails;
import com.creator.imageAndMusic.domain.dto.BoardDto;
import com.creator.imageAndMusic.domain.dto.Criteria;
import com.creator.imageAndMusic.domain.dto.PageDto;
import com.creator.imageAndMusic.domain.dto.UserDto;
import com.creator.imageAndMusic.domain.entity.*;
import com.creator.imageAndMusic.domain.service.BookmarkServiceImpl;
import com.creator.imageAndMusic.domain.service.FavoriteImageServiceImpl;
import com.creator.imageAndMusic.domain.service.ImageRankingService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @Autowired
    private FavoriteImageServiceImpl favoriteImageService;

    @Autowired
    private BookmarkServiceImpl bookmarkService;

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
                       @RequestParam(name="mode",defaultValue="1") Integer mode,
                       Authentication authentication,
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
        PageDto likepageDto = (PageDto) map.get("likepageDto");
        List<ImagesRanking> list = (List<ImagesRanking>) map.get("list");
        List<ImagesRanking> likelist = (List<ImagesRanking>) map.get("likelist");
        List<ImagesRanking> rankingList = (List<ImagesRanking>) map.get("rankingList");
        List<ImagesRanking> rankingLikeList = (List<ImagesRanking>) map.get("rankingLikeList");
        int count = (int)map.get("count");


        model.addAttribute("pageNo",pageNo);
        model.addAttribute("pageDto",pageDto);
        model.addAttribute("likepageDto",likepageDto);
        model.addAttribute("list",list);
        model.addAttribute("likelist",likelist);
        model.addAttribute("rankingList",rankingList);
        model.addAttribute("rankingLikeList",rankingLikeList);
        model.addAttribute("mode",mode);
        //ThumbUp 찾기
        List<FavoriteImage> favoriteImageList  = favoriteImageService.getMyfavoriteImage(authentication.getName());
        model.addAttribute("favoriteList",favoriteImageList);
        //Bookmark 찾기
        List<Bookmark> bookmarkList  =bookmarkService.getMyBookmark(authentication.getName());
        model.addAttribute("bookmarkList",bookmarkList);

        //좋아요 순서


        return "imageRanking/list";

    }

    @GetMapping("/read")
    public void readRanking(@RequestParam(name = "rankingId",defaultValue = "1") Long rankingId,Model model){
        log.info("GET /imageRanking/read..");


        ImagesRanking imagesRanking = imageRankingService.getImageRanking(rankingId);
        model.addAttribute("imagesRanking",imagesRanking);
        model.addAttribute("title","조회순");


        List<ImageReply> list =  imageRankingService.getAllReply(imagesRanking.getImagesFileInfo().getImages().getIamgeid());

        model.addAttribute("replyList",list);
        model.addAttribute("total",list.size());


        imageRankingService.count(rankingId);
    }

    @GetMapping("/local")
    public String local(Model model){
        log.info("GET /around/local");

        Map<String,Object> result = imageRankingService.getLocalImageRanking();

        model.addAttribute("result",result);


        return "imageRanking/local";
    }


    @GetMapping("/cat")
    public void cat(
            @RequestParam(value = "subCategory",defaultValue = "Character") String subCategory,
            Model model
    ){
       log.info("GET /imageRanking/cat...");
        List<ImagesRanking> list =   imageRankingService.getAllImageRankingByCategory(subCategory);
        System.out.println("개수 : " + list.size());
        model.addAttribute("imageList",list);
        model.addAttribute("subCategory",subCategory);
    }

    @GetMapping("/reply/add")
    public @ResponseBody ResponseEntity<ImageReply> replyAdd(
            @RequestParam("context") String context,
            @RequestParam("imageId") Long imageId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
            ){


        System.out.println("/GET /image/reply/add...context : " + context + " , imageId : " + imageId);
        UserDto userDto =  principalDetails.getUserDto();

        ImageReply reply  =  imageRankingService.addReply(context,imageId,userDto.getUsername());


        if(reply==null)
            return new ResponseEntity<>(reply,HttpStatus.BAD_GATEWAY);

        return new ResponseEntity<>(reply,HttpStatus.OK);

    }

}
