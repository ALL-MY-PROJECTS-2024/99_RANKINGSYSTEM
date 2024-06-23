package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.domain.entity.Bookmark;
import com.creator.imageAndMusic.domain.entity.MusicBookmark;
import com.creator.imageAndMusic.domain.service.BookmarkServiceImpl;
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
public class BookmarkController {


    @Autowired
    private BookmarkServiceImpl bookmarkService;
    @GetMapping("/my")
    public String my(Model model, Authentication authentication){
        log.info("GET /bookmark/my..");
        List<Bookmark> list =  bookmarkService.getBookmark(authentication.getName());
        List<MusicBookmark> musicList = bookmarkService.getMusicBookmark(authentication.getName());

        model.addAttribute("list",list);
        model.addAttribute("musicList",musicList);
        return "user/bookmark/my";
    }

    @GetMapping("/del/{id}")
    public @ResponseBody ResponseEntity<String> del(@PathVariable("id") Long id){
        bookmarkService.deleteBookmark(id);
        return new ResponseEntity("SUCCESS",HttpStatus.OK);
    }
    @GetMapping("/add/{rankingId}")
    public @ResponseBody ResponseEntity<String> add(@PathVariable("rankingId") Long rankingId, Authentication authentication){
        System.out.println("rankingId : " + rankingId);
        Map<String,Object> result=  bookmarkService.addBookmark(rankingId,authentication.getName());
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/add/music/{rankingId}")
    public @ResponseBody ResponseEntity<String> add_music(@PathVariable("rankingId") Long rankingId, Authentication authentication){
        System.out.println("rankingId : " + rankingId);
        Map<String,Object> result=  bookmarkService.addMusicBookmark(rankingId,authentication.getName());
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
