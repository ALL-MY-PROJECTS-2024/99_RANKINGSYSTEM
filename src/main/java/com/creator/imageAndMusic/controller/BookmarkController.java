package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.domain.entity.Bookmark;
import com.creator.imageAndMusic.domain.entity.MusicBookmark;
import com.creator.imageAndMusic.domain.service.BookmarkServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/bookmark")
@Slf4j
@Tag(name = "즐겨찾기", description = "이미지/음악 즐겨찾기추가,삭제")
public class BookmarkController {


    @Autowired
    private BookmarkServiceImpl bookmarkService;


    @Operation(
            summary = "홈>즐겨찾기",
            description = "이미지/음악  즐겨찾기 목록 확인",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/my")
    public String my(Model model, Authentication authentication){
        log.info("GET /bookmark/my..");
        List<Bookmark> list =  bookmarkService.getBookmark(authentication.getName());
        List<MusicBookmark> musicList = bookmarkService.getMusicBookmark(authentication.getName());

        model.addAttribute("list",list);
        model.addAttribute("musicList",musicList);
        return "user/bookmark/my";
    }

    @Operation(
            summary = "홈>즐겨찾기",
            description = "이미지  즐겨찾기 삭제 처리",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/del/{id}")
    public @ResponseBody ResponseEntity<String> del(@PathVariable("id") Long id){
        bookmarkService.deleteBookmark(id);
        return new ResponseEntity("SUCCESS",HttpStatus.OK);
    }
    @Operation(
            summary = "홈>즐겨찾기",
            description = "음악  즐겨찾기 삭제 처리",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/del/music/{id}")
    public @ResponseBody ResponseEntity<String> del_music(@PathVariable("id") Long id){
        bookmarkService.deleteMusicBookmark(id);
        return new ResponseEntity("SUCCESS",HttpStatus.OK);
    }
    @Operation(
            summary = "홈>즐겨찾기",
            description = "이미지 즐겨찾기 추가",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/add/{rankingId}")
    public @ResponseBody ResponseEntity<String> add(@PathVariable("rankingId") Long rankingId, Authentication authentication){
        System.out.println("rankingId : " + rankingId);
        Map<String,Object> result=  bookmarkService.addBookmark(rankingId,authentication.getName());
        return new ResponseEntity(result, HttpStatus.OK);
    }


    @Operation(
            summary = "홈>즐겨찾기",
            description = "음악 즐겨찾기 추가",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/add/music/{rankingId}")
    public @ResponseBody ResponseEntity<String> add_music(@PathVariable("rankingId") Long rankingId, Authentication authentication){
        System.out.println("rankingId : " + rankingId);
        Map<String,Object> result=  bookmarkService.addMusicBookmark(rankingId,authentication.getName());
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
