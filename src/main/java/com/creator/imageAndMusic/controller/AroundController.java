package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.domain.entity.ImagesFileInfo;
import com.creator.imageAndMusic.domain.entity.ImagesRanking;
import com.creator.imageAndMusic.domain.entity.MusicFileInfo;
import com.creator.imageAndMusic.domain.entity.MusicRanking;
import com.creator.imageAndMusic.domain.repository.ImageRankingRepository;
import com.creator.imageAndMusic.domain.service.AroundServiceImpl;
import com.creator.imageAndMusic.domain.service.ImageRankingServiceImpl;
import com.creator.imageAndMusic.domain.service.MusicRankingServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.function.DoubleToIntFunction;

@Controller
@Slf4j
@RequestMapping("/around")
public class AroundController {

    @Autowired
    private AroundServiceImpl aroundService;

    @Autowired
    private ImageRankingServiceImpl imageRankingServiceImpl;

    @Autowired
    private MusicRankingServiceImpl musicRankingServiceImpl;
    @GetMapping("/popular")
    public void popular(Model model){
        log.info("GET /around/popular");

       Map<String,Object> map1 =  imageRankingServiceImpl.getImageRankingPopular();
       List<ImagesRanking> imageTop10ByCount = (List<ImagesRanking>) map1.get("imageTop10ByCount");
       List<ImagesRanking> imageTop10ByLike = (List<ImagesRanking>) map1.get("imageTop10ByLike");

       Map<String,Object> map2 = musicRankingServiceImpl.getMusicRankingPopular();
       List<MusicRanking> musicTop10ByCount = (List<MusicRanking>) map2.get("musicTop10ByCount");
       List<MusicRanking> musicTop10ByLike = (List<MusicRanking>) map2.get("musicTop10ByLike");


       model.addAttribute("imageTop10ByCount",imageTop10ByCount);
       model.addAttribute("imageTop10ByLike",imageTop10ByLike);
       model.addAttribute("musicTop10ByCount",musicTop10ByCount);
       model.addAttribute("musicTop10ByLike",musicTop10ByLike);

    }

    @GetMapping("/group")
    public void group(
            @RequestParam(value = "mainCategory",defaultValue = "이미지") String mainCategory,
            @RequestParam(value = "subCategory",defaultValue = "Character") String subCategory,
            @RequestParam(value = "mode",defaultValue = "1") String mode,
            Model model
    ){
        log.info("GET /around/group " + mainCategory + " " + subCategory + " " + mode);
        if(mainCategory.contains("이미지")){
           List<ImagesRanking> list =   imageRankingServiceImpl.getAllImageRankingByCategory(subCategory);
           model.addAttribute("list",list);
        }
        else{
            List<MusicRanking> list =   musicRankingServiceImpl.getAllMusicRankingByCategory(subCategory);
            model.addAttribute("list",list);
        }

        model.addAttribute("mode",mode);
    }
    @GetMapping("/local")
    public String local(Model model){
        log.info("GET /around/local");

        List<ImagesRanking> list =  imageRankingServiceImpl.getAllImageRanking();

        //List<ImagesFileInfo> list =  aroundService.getAllImages();
        for(ImagesRanking info : list){
            System.out.println(info);
        }
        model.addAttribute("list",list);

        return "around/local";
    }
    @GetMapping("/global")
    public String global(Model model){
        log.info("GET /around/global");
        List<ImagesRanking> list =  imageRankingServiceImpl.getAllImageRanking();
        for(ImagesRanking info : list){
            System.out.println(info);
        }
        model.addAttribute("list",list);
        return "around/global";

    }
}
