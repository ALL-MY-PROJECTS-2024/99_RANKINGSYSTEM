package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.domain.dto.TradingImageDto;
import com.creator.imageAndMusic.domain.dto.TradingMusicDto;
import com.creator.imageAndMusic.domain.entity.ImagesRanking;
import com.creator.imageAndMusic.domain.entity.MusicRanking;
import com.creator.imageAndMusic.domain.entity.TradingImage;
import com.creator.imageAndMusic.domain.entity.TradingMusic;
import com.creator.imageAndMusic.domain.repository.ImagesRepository;
import com.creator.imageAndMusic.domain.service.ImageRankingServiceImpl;
import com.creator.imageAndMusic.domain.service.MusicRankingServiceImpl;
import com.creator.imageAndMusic.domain.service.TradingImageServiceImpl;
import com.creator.imageAndMusic.domain.service.TradingMusicServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@Slf4j
public class HomeController {


    @Autowired
    private TradingImageServiceImpl tradingImageServiceImpl;
    @Autowired
    private TradingMusicServiceImpl tradingMusicServiceImpl;

    @Autowired
    private ImageRankingServiceImpl imageRankingServiceImpl;

    @Autowired
    private MusicRankingServiceImpl musicRankingServiceImpl;


    @GetMapping("/")
    public String home(Model model){
        log.info("GET / ...");
        //--------------------------------------------------------
        //
        //--------------------------------------------------------
        List<TradingImage> listEntity =  tradingImageServiceImpl.getAllTradingImages();

        List<TradingImageDto> list = new ArrayList<>();

        listEntity.forEach(entity ->{
            TradingImageDto dto = new TradingImageDto();
            dto.setTradingid(entity.getTradingid());
            dto.setTitle("[IMG]_"+entity.getTradingid()+"_"+  entity.getStatus());
            dto.setSeller((entity.getSeller()!=null)?entity.getSeller().getUsername():null);
            dto.setBuyer( (entity.getBuyer()!=null)?entity.getBuyer().getUsername():null);
            dto.setFileid(entity.getFileid().getFileid());
            dto.setAdminAccepted(entity.isAdminAccepted());
            dto.setAuctionStartTime(entity.getAuctionStartTime());
            dto.setAuctionEndTime(entity.getAuctionEndTime());
            dto.setPrice(entity.getPrice());
            dto.setPaymentState(entity.isPaymentState());
            dto.setStatus(entity.getStatus());
            dto.setFileDir(entity.getFileid().getDir());
            dto.setFilename(entity.getFileid().getFilename());
            dto.setCur(entity.getCur());
            dto.setAuctionState(entity.getAuctionState());
            dto.setMax(entity.getMax());
            dto.setMembers(entity.getMembers());
            dto.setRoomId(entity.getRoomId());
            dto.setStartPrice(entity.getStartPrice());

            //채팅방
            String roomId = entity.getRoomId();

            list.add(dto);
        });

        List<TradingMusic> listEntity2 =  tradingMusicServiceImpl.getAllTradingMusic();

        List<TradingMusicDto> list2 = new ArrayList<>();

        listEntity2.forEach(entity ->{
            TradingMusicDto dto = new TradingMusicDto();
            dto.setTradingid(entity.getTradingid());
            dto.setTitle("[MUSIC]_"+entity.getTradingid()+"_"+  entity.getStatus());
            dto.setSeller((entity.getSeller()!=null)?entity.getSeller().getUsername():null);
            dto.setBuyer( (entity.getBuyer()!=null)?entity.getBuyer().getUsername():null);
            dto.setFileid(entity.getFileid().getFileid());
            dto.setAdminAccepted(entity.isAdminAccepted());
            dto.setAuctionStartTime(entity.getAuctionStartTime());
            dto.setAuctionEndTime(entity.getAuctionEndTime());
            dto.setPrice(entity.getPrice());
            dto.setPaymentState(entity.isPaymentState());
            dto.setStatus(entity.getStatus());
            dto.setFileDir(entity.getFileid().getDir());
            dto.setFilename(entity.getFileid().getFilename());
            dto.setCur(entity.getCur());
            dto.setAuctionState(entity.getAuctionState());
            dto.setMax(entity.getMax());
            dto.setMembers(entity.getMembers());
            dto.setRoomId(entity.getRoomId());
            dto.setStartPrice(entity.getStartPrice());

            //채팅방
            String roomId = entity.getRoomId();

            list2.add(dto);
        });

        //--------------------------------------------------------
        // 카테고리별 개수 가져오기
        //--------------------------------------------------------
        Map<String,Object> imageCountMap =  imageRankingServiceImpl.getAllImageRankingByAllCategoryCount();
        Map<String,Object> musicCountMap =  musicRankingServiceImpl.getAllMusicRankingByAllCategoryCount();


        Map<String,Object> imageRankingPopular =  imageRankingServiceImpl.getImageRankingPopular();
        List<ImagesRanking> imageTop10ByCount = (List<ImagesRanking>) imageRankingPopular.get("imageTop10ByCount");
        List<ImagesRanking> imageTop10ByLike = (List<ImagesRanking>) imageRankingPopular.get("imageTop10ByLike");
        Map<String,Object> musicRankingPopular = musicRankingServiceImpl.getMusicRankingPopular();
        List<MusicRanking> musicTop10ByCount = (List<MusicRanking>) musicRankingPopular.get("musicTop10ByCount");
        List<MusicRanking> musicTop10ByLike = (List<MusicRanking>) musicRankingPopular.get("musicTop10ByLike");

        System.out.println(imageTop10ByCount);
        model.addAttribute("list",list);
        model.addAttribute("list2",list2);
        model.addAttribute("imageCountMap",imageCountMap);
        model.addAttribute("musicCountMap",musicCountMap);
        model.addAttribute("imageTop10ByCount",imageTop10ByCount);
        model.addAttribute("imageTop10ByLike",imageTop10ByLike);
        model.addAttribute("musicTop10ByCount",musicTop10ByCount);
        model.addAttribute("musicTop10ByLike",musicTop10ByLike);


        return "index";
    }




    @GetMapping("/login")
    public void login(){
        log.info("GET /login...");
    }



}
