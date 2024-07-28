package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.config.auth.PrincipalDetails;
import com.creator.imageAndMusic.domain.dto.Criteria;
import com.creator.imageAndMusic.domain.dto.PageDto;
import com.creator.imageAndMusic.domain.dto.UserDto;
import com.creator.imageAndMusic.domain.entity.*;
import com.creator.imageAndMusic.domain.service.*;

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

@Slf4j
@Controller
@RequestMapping("/musicRanking")
public class MusicRankingController {


    @ExceptionHandler
    private void exceptionHandler(Exception e){
        log.info("/imageRanking Exception.. ",e);
    }
    @Autowired
    private ImageRankingServiceImpl imageRankingService;

    @Autowired
    private FavoriteImageServiceImpl favoriteImageService;

    @Autowired
    private FavoriteMusicServiceImpl favoriteMusicService;
    @Autowired
    private MusicRankingServiceImpl musicRankingService;

    @Autowired
    private BookmarkServiceImpl bookmarkService;

    @GetMapping("/add")
    public @ResponseBody ResponseEntity<String> addRanking(@RequestParam("fileid") Long fileid,Model model) throws Exception {
        log.info("imageRanking/add..fileid : " + fileid);

        boolean isAdded =  musicRankingService.addRankingImage(fileid);
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
        log.info("GET /musicRanking/list... ");

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
        Map<String,Object> map =  musicRankingService.getAllMusicRanking(criteria);

        PageDto pageDto = (PageDto) map.get("pageDto");
        PageDto likepageDto = (PageDto) map.get("likepageDto");
        List<MusicRanking> list = (List<MusicRanking>) map.get("list");
        List<MusicRanking> likelist = (List<MusicRanking>) map.get("likelist");
        List<MusicRanking> rankingList = (List<MusicRanking>) map.get("rankingList");
        List<MusicRanking> rankingLikeList = (List<MusicRanking>) map.get("rankingLikeList");
        int count = (int)map.get("count");


        model.addAttribute("pageNo",pageNo);
        model.addAttribute("pageDto",pageDto);
        model.addAttribute("likepageDto",likepageDto);
        model.addAttribute("list",list);
        model.addAttribute("likelist",likelist);
        model.addAttribute("rankingList",rankingList);
        model.addAttribute("rankingLikeList",rankingLikeList);
        model.addAttribute("mode",mode);

//        //ThumbUp 찾기
        List<FavoriteMusic> favoriteMusicList  = favoriteMusicService.getMyfavoriteMusic(authentication.getName());
        model.addAttribute("favoriteList",favoriteMusicList);

        //Bookmark 찾기
        List<MusicBookmark> bookmarkList  =bookmarkService.getMyBookmarkMusic(authentication.getName());
        model.addAttribute("bookmarkList",bookmarkList);


        return "musicRanking/list";

    }

    @GetMapping("/read")
    public void readRanking(@RequestParam(name = "rankingId",defaultValue = "1") Long rankingId,Model model){
        log.info("GET /musicRanking/read..");

        MusicRanking musicRanking = musicRankingService.getMusicRanking(rankingId);
        model.addAttribute("musicRanking",musicRanking);
        model.addAttribute("title","조회순");

        List<MusicReply> list =  musicRankingService.getAllReply(musicRanking.getMusicFileInfo().getMusic().getMusicid());

        model.addAttribute("replyList",list);
        model.addAttribute("total",list.size());

        musicRankingService.count(rankingId);

    }
    @GetMapping("/local")
    public String local(Model model){
        log.info("GET /around/local");

        Map<String,Object> result = musicRankingService.getLocalMusicRanking();

        model.addAttribute("result",result);

        return "musicRanking/local";
    }

    @GetMapping("/cat")
    public void cat( @RequestParam(value = "subCategory",defaultValue = "Jazz") String subCategory,Model model){
        log.info("GET /musicRanking/cat...");
        List<MusicRanking> list =   musicRankingService.getAllMusicRankingByCategory(subCategory);

        System.out.println("개수 : " + list.size());
        model.addAttribute("musicList",list);
        model.addAttribute("subCategory",subCategory);


    }
    @GetMapping("/reply/add")
    public @ResponseBody ResponseEntity<MusicReply> replyAdd(
            @RequestParam("context") String context,
            @RequestParam("musicId") Long musicId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){


        System.out.println("/GET /image/reply/add...context : " + context + " , musicId : " + musicId);
        UserDto userDto =  principalDetails.getUserDto();

        MusicReply reply  =  musicRankingService.addReply(context,musicId,userDto.getUsername());

        if(reply==null)
            return new ResponseEntity<>(reply,HttpStatus.BAD_GATEWAY);

        return new ResponseEntity<>(reply,HttpStatus.OK);

    }
    @DeleteMapping("/reply/delete")
    public @ResponseBody ResponseEntity<String> delete(
            @RequestParam("id") Long id,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    )
    {
        Map<String,Object> result=musicRankingService.deleteReply(id,principalDetails);

        String message =(String)result.get("message");
        boolean success = (boolean)result.get("success");
        if(!success)
            return new ResponseEntity<>(message,HttpStatus.BAD_GATEWAY);

        return new ResponseEntity<>(message,HttpStatus.OK);
    }


}
