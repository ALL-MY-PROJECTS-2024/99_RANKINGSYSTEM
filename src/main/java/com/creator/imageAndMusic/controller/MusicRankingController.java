package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.config.auth.PrincipalDetails;
import com.creator.imageAndMusic.domain.dto.Criteria;
import com.creator.imageAndMusic.domain.dto.PageDto;
import com.creator.imageAndMusic.domain.dto.UserDto;
import com.creator.imageAndMusic.domain.entity.*;
import com.creator.imageAndMusic.domain.service.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/musicRanking")
@Tag(name = "음악 랭킹", description = "음악랭킹에 추가,랭킹 목록,랭킹 조회")
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

    @Operation(
            summary = "홈>내 이미지>갤러리",
            description = "갤러리 내 음악을 랭킹에 추가하는 기능",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/add")
    public @ResponseBody ResponseEntity<String> addRanking(@RequestParam("fileid") Long fileid,Model model) throws Exception {
        log.info("imageRanking/add..fileid : " + fileid);

        boolean isAdded =  musicRankingService.addRankingImage(fileid);
        if(isAdded)
            return  new ResponseEntity("RANKING 등록 완료",HttpStatus.OK);
        else
            return  new ResponseEntity("RANKING 등록 실패",HttpStatus.BAD_GATEWAY);
    }

    @Operation(
            summary = "홈>이달의콘텐츠RANKING>전체 음악 랭킹",
            description = "전체 음악 랭킹 목록 페이지",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
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

    @Operation(
            summary = "홈>이달의콘텐츠RANKING>전체 음악 랭킹>읽기",
            description = "전체 음악 랭킹 읽기 페이지",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
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

    @Operation(
            summary = "홈>이달의콘텐츠RANKING>지역별 랭킹",
            description = "지역별 음악 랭킹 페이지",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/local")
    public String local(Model model){
        log.info("GET /around/local");

        Map<String,Object> result = musicRankingService.getLocalMusicRanking();

        model.addAttribute("result",result);

        return "musicRanking/local";
    }

    @Operation(
            summary = "홈>이달의콘텐츠RANKING>분야별 랭킹",
            description = "분야별 음악 랭킹 페이지",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/cat")
    public void cat(
            @RequestParam(value = "subCategory",defaultValue = "Jazz") String subCategory,
            @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
            @RequestParam(value = "amount",defaultValue = "10") int amount,
            Model model
    ){
        log.info("GET /musicRanking/cat...");
        Criteria criteria= new Criteria(pageNo,amount); //페이지이동 요청 했을때


        Map<String,Object> result  =   musicRankingService.getAllMusicRankingByCategory(subCategory,criteria);
        List<MusicRanking> list = (List<MusicRanking>)result.get("list");
        PageDto pageDto = (PageDto)result.get("pageDto");
        int totalCount = (int)result.get("totalCount");

        System.out.println("개수 : " + list.size());
        model.addAttribute("musicList",list);
        model.addAttribute("pageDto",pageDto);
        model.addAttribute("totalCount",totalCount);

        model.addAttribute("subCategory",subCategory);
        model.addAttribute("amount",amount);
    }


    @Operation(
            summary = "홈>이달의콘텐츠RANKING>분야별 랭킹",
            description = "분야별 음악 랭킹 페이징 처리",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/cat/next")
    public @ResponseBody Map<String,Object> getNext(
            @RequestParam(value = "subCategory",defaultValue = "Jazz") String subCategory,
            @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
            @RequestParam(value = "amount",defaultValue = "10") int amount
    ){

        Criteria criteria= new Criteria(pageNo,amount); //페이지이동 요청 했을때
        Map<String,Object> returnValue = new HashMap<>();

        log.info("GET /musicRanking/cat/next..." +subCategory);
        Map<String,Object> result =   musicRankingService.getAllMusicRankingByCategory(subCategory,criteria);
        List<ImagesRanking> list = (List<ImagesRanking>)result.get("list");
        PageDto pageDto = (PageDto)result.get("pageDto");
        int totalCount = (int)result.get("totalCount");
        System.out.println("개수 : " + list.size());
        returnValue.put("musicList",list);
        returnValue.put("pageDto",pageDto);
        returnValue.put("totalCount",totalCount);

        returnValue.put("subCategory",subCategory);
        returnValue.put("amount",amount);
        return returnValue;
    }


    @Operation(
            summary = "홈>이달의콘텐츠RANKING>전체이미지랭킹>읽기",
            description = "음악 댓글 추가",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
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


    @Operation(
            summary = "홈>이달의콘텐츠RANKING>전체이미지랭킹>읽기",
            description = "음악 댓글 삭제",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
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
