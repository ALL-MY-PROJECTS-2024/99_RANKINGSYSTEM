package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.domain.dto.TradingImageDto;
import com.creator.imageAndMusic.domain.entity.TradingImage;
import com.creator.imageAndMusic.domain.service.TradingImageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private TradingImageServiceImpl tradingImageService;
    @GetMapping("/main")
    public void main(Model model){
        log.info("/GET /admin/main");
    }
    @GetMapping("/trading/main")
    public void trading_main(Model model){
        log.info("/GET /admin/trade/main");
        List<TradingImage> list = tradingImageService.getAllTradingImages();
        model.addAttribute("list" ,list);
    }
    @GetMapping("/trading/accept")
    public String trading_accept(@ModelAttribute TradingImageDto dto, RedirectAttributes rttr){
        log.info("/GET /admin/trade/accept");
        boolean isAccepted = tradingImageService.acceptTradingImages(dto);
        if(isAccepted){
            rttr.addFlashAttribute("message","id : "+dto.getTradingid()+ "승인하였습니다");
            return "redirect:/admin/trading/main";
        }
        rttr.addFlashAttribute("message","id : "+dto.getTradingid()+ "승인실패..");
        return "redirect:/admin/trading/main";

    }
    @GetMapping("/trading/cancel")
    public String trading_cancel(@RequestParam("tradingid") Long tradingId, RedirectAttributes rttr){
        log.info("/GET /admin/trade/cancel");
        boolean isAccepted = tradingImageService.cancelTradingImages(tradingId);
        if(isAccepted){
            rttr.addFlashAttribute("message","id : "+tradingId+ "승인 취소하였습니다");
            return "redirect:/admin/trading/main";
        }
        rttr.addFlashAttribute("message","id : "+tradingId+ "취소 실패..");
        return "redirect:/admin/trading/main";

    }
}
