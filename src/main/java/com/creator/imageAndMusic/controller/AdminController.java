package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.domain.dto.TradingImageDto;
import com.creator.imageAndMusic.domain.dto.TradingMusicDto;
import com.creator.imageAndMusic.domain.entity.ConnectionUser;
import com.creator.imageAndMusic.domain.entity.TradingImage;
import com.creator.imageAndMusic.domain.entity.TradingMusic;
import com.creator.imageAndMusic.domain.repository.ConnectionUserRepository;
import com.creator.imageAndMusic.domain.service.SettingServiceImpl;
import com.creator.imageAndMusic.domain.service.TradingImageServiceImpl;
import com.creator.imageAndMusic.domain.service.TradingMusicServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/admin")
@Tag(name = "관리자 설정", description = "접속 현황,API DOCUMENT(SWAGGER),경매환경설정,업로드환경설정,관리자KEY설정")
public class AdminController {

    @Autowired
    private TradingImageServiceImpl tradingImageService;

    @Autowired
    private TradingMusicServiceImpl tradingMusicService;

    @Autowired
    private SettingServiceImpl settingService;

    @GetMapping("/main")
    public void main(Model model){
        log.info("/GET /admin/main");
    }


    @Operation(
            summary = "홈>TRADING>이미지/메인",
            description = "세계지도 페이지입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/trading/image/main")
    public void trading_main(Model model){
        log.info("/GET /admin/trade/main");
        List<TradingImage> list = tradingImageService.getAllTradingImages();
        model.addAttribute("list" ,list);
    }


    @Operation(
            summary = "홈>둘러보기>세계지도",
            description = "세계지도 페이지입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/trading/image/accept")
    public String trading_accept(@ModelAttribute TradingImageDto dto, RedirectAttributes rttr){
        log.info("/GET /admin/trade/accept");
        boolean isAccepted = tradingImageService.acceptTradingImages(dto);
        if(isAccepted){
            rttr.addFlashAttribute("message","id : "+dto.getTradingid()+ "승인하였습니다");
            return "redirect:/admin/trading/image/main";
        }
        rttr.addFlashAttribute("message","id : "+dto.getTradingid()+ "승인실패..");
        return "redirect:/admin/trading/image/main";

    }

    @Operation(
            summary = "홈>둘러보기>세계지도",
            description = "세계지도 페이지입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/trading/image/cancel")
    public String trading_cancel(@RequestParam("tradingid") Long tradingId, RedirectAttributes rttr){
        log.info("/GET /admin/trade/cancel");
        boolean isAccepted = tradingImageService.cancelTradingImages(tradingId);
        if(isAccepted){
            rttr.addFlashAttribute("message","id : "+tradingId+ "승인 취소하였습니다");
            return "redirect:/admin/trading/image/main";
        }
        rttr.addFlashAttribute("message","id : "+tradingId+ "취소 실패..");
        return "redirect:/admin/trading/image/main";

    }
    //----------------------------------------------------------------
    //음악
    //----------------------------------------------------------------

    @Operation(
            summary = "홈>둘러보기>세계지도",
            description = "세계지도 페이지입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/trading/music/main")
    public void trading_main_music(Model model){
        log.info("/GET /admin/trade/main");
        List<TradingMusic> list = tradingMusicService.getAllTradingMusic();
        model.addAttribute("list" ,list);
    }



    @Operation(
            summary = "홈>둘러보기>세계지도",
            description = "세계지도 페이지입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/trading/music/accept")
    public String trading_accept_music(@ModelAttribute TradingMusicDto dto, RedirectAttributes rttr){
        log.info("/GET /admin/trade/accept");
        boolean isAccepted = tradingMusicService.acceptTradingMusic(dto);
        if(isAccepted){
            rttr.addFlashAttribute("message","id : "+dto.getTradingid()+ "승인하였습니다");
            return "redirect:/admin/trading/music/main";
        }
        rttr.addFlashAttribute("message","id : "+dto.getTradingid()+ "승인실패..");
        return "redirect:/admin/trading/music/main";

    }

    @Operation(
            summary = "홈>둘러보기>세계지도",
            description = "세계지도 페이지입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/trading/music/cancel")
    public String trading_cancel_music(@RequestParam("tradingid") Long tradingId, RedirectAttributes rttr){
        log.info("/GET /admin/trade/cancel");
        boolean isAccepted = tradingMusicService.cancelTradingMusic(tradingId);
        if(isAccepted){
            rttr.addFlashAttribute("message","id : "+tradingId+ "승인 취소하였습니다");
            return "redirect:/admin/trading/main";
        }
        rttr.addFlashAttribute("message","id : "+tradingId+ "취소 실패..");
        return "redirect:/admin/trading/main";

    }
    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------

    @Autowired
    private ConnectionUserRepository connectionUserRepository;


    @Operation(
            summary = "홈>둘러보기>세계지도",
            description = "세계지도 페이지입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/settings")
    public void settings(Model model){

        log.info("GET /admin/settings...");

        Map<String,Object> result = settingService.getSettingInfo();
        List<ConnectionUser> totalConnectionList = (List<ConnectionUser>)result.get("totalConnectionList");
        model.addAttribute("totalConnectionList",totalConnectionList);

    }

}
