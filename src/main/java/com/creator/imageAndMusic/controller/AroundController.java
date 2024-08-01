package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.domain.dto.Criteria;
import com.creator.imageAndMusic.domain.dto.PageDto;
import com.creator.imageAndMusic.domain.entity.ImagesRanking;
import com.creator.imageAndMusic.domain.entity.MusicRanking;
import com.creator.imageAndMusic.domain.service.AroundServiceImpl;
import com.creator.imageAndMusic.domain.service.ImageRankingServiceImpl;
import com.creator.imageAndMusic.domain.service.MusicRankingServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/around")
@Tag(name = "둘러보기", description = "인기이미지,그룹,국내지도,세계지도")
public class AroundController {

    @Autowired
    private AroundServiceImpl aroundService;

    @Autowired
    private ImageRankingServiceImpl imageRankingServiceImpl;

    @Autowired
    private MusicRankingServiceImpl musicRankingServiceImpl;

    @Operation(
            summary = "홈>둘러보기>인기이미지",
            description = "이미지TOP 10건 과 음악TOP 10건을 표시합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
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


    @Operation(
            summary = "홈>둘러보기>그룹",
            description = "이미지/음악 세부 카테고리별 ITEM을 표시합니다",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/group")
    public void group(
            @RequestParam(value = "mainCategory",defaultValue = "이미지") String mainCategory,
            @RequestParam(value = "subCategory",defaultValue = "Character") String subCategory,
            @RequestParam(value = "mode",defaultValue = "1") String mode,

            @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
            @RequestParam(value = "amount",defaultValue = "10") int amount,
            Model model
    ){

        Criteria criteria= new Criteria(pageNo,amount); //페이지이동 요청 했을때


        log.info("GET /around/group " + mainCategory + " " + subCategory + " " + mode);
        if(mainCategory.contains("이미지")){
           Map<String,Object> result =   imageRankingServiceImpl.getAllImageRankingByCategory(subCategory,criteria);
           List<ImagesRanking> list = (List<ImagesRanking>)result.get("list");
           PageDto pageDto = (PageDto)result.get("pageDto");
           int totalCount = (int)result.get("totalCount");
           System.out.println("개수 : " + list.size());
           model.addAttribute("imageList",list);
           model.addAttribute("pageDto",pageDto);
           model.addAttribute("totalCount",totalCount);
        }
        else{
            Map<String,Object> result =   musicRankingServiceImpl.getAllMusicRankingByCategory(subCategory,criteria);
            List<MusicRanking> list = (List<MusicRanking>)result.get("list");
            PageDto pageDto = (PageDto)result.get("pageDto");
            int totalCount = (int)result.get("totalCount");
            System.out.println("개수 : " + list.size());
            model.addAttribute("musicList",list);
            model.addAttribute("pageDto",pageDto);
            model.addAttribute("totalCount",totalCount);
        }

        model.addAttribute("subCategory",subCategory);
        model.addAttribute("mainCategory",mainCategory);
        model.addAttribute("mode",mode);
        model.addAttribute("amount",amount);
    }

    @Operation(
            summary = "홈>둘러보기>그룹(페이징)",
            description = "그룹 페이지의 페이징 처리요청 입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "수량(amount)에 따른 LISTITEM이 반환됩니다.\n페이징 처리를 위한 객체(PageDto)가 반환됩니다.\n전체 ITEM건수(totalCount)가 반환됩니다."
                    )
            }
    )
    @GetMapping("/group/next")
    public @ResponseBody Map<String,Object> getNext(
            @RequestParam(value = "mainCategory",defaultValue = "이미지") String mainCategory,
            @RequestParam(value = "subCategory",defaultValue = "Character") String subCategory,
            @RequestParam(value = "mode",defaultValue = "1") String mode,

            @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
            @RequestParam(value = "amount",defaultValue = "10") int amount

    ){

        Criteria criteria= new Criteria(pageNo,amount); //페이지이동 요청 했을때
        Map<String,Object> returnValue = new HashMap<>();

        log.info("GET /around/group/next " + mainCategory + " " + subCategory + " " + mode);
        if(mainCategory.contains("이미지")){
            Map<String,Object> result =   imageRankingServiceImpl.getAllImageRankingByCategory(subCategory,criteria);
            List<ImagesRanking> list = (List<ImagesRanking>)result.get("list");
            PageDto pageDto = (PageDto)result.get("pageDto");
            int totalCount = (int)result.get("totalCount");
            System.out.println("개수 : " + list.size());
            returnValue.put("imageList",list);
            returnValue.put("pageDto",pageDto);
            returnValue.put("totalCount",totalCount);
        }
        else{
            Map<String,Object> result =   musicRankingServiceImpl.getAllMusicRankingByCategory(subCategory,criteria);
            List<MusicRanking> list = (List<MusicRanking>)result.get("list");
            PageDto pageDto = (PageDto)result.get("pageDto");
            int totalCount = (int)result.get("totalCount");
            System.out.println("개수 : " + list.size());
            returnValue.put("musicList",list);
            returnValue.put("pageDto",pageDto);
            returnValue.put("totalCount",totalCount);
        }

        returnValue.put("subCategory",subCategory);
        returnValue.put("mainCategory",mainCategory);
        returnValue.put("mode",mode);
        returnValue.put("amount",amount);
        return returnValue;
    }

    @Operation(
            summary = "홈>둘러보기>국내지도",
            description = "국내지도 페이지입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = ""
                    )
            }
    )
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
