package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.domain.dto.PaymentDto;
import com.creator.imageAndMusic.domain.service.PaymentImageServiceImpl;
import com.creator.imageAndMusic.domain.service.PaymentMusicServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentMusicServiceImpl paymentMusicService;
    @Autowired
    private PaymentImageServiceImpl paymentImageService;
    @PostMapping("/add/image")
    public ResponseEntity<String> payment_insert(@RequestBody PaymentDto paymentDto) throws IOException {
        log.info("/payment/add/image..." + paymentDto);
        paymentImageService.addPayment(paymentDto);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
    @PostMapping("/add/music")
    public ResponseEntity<String> payment_insert_music(@RequestBody PaymentDto paymentDto) throws IOException {
        log.info("/add/music..." + paymentDto);
        paymentMusicService.addPayment(paymentDto);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
    //https://hyphen.im/product-api/view?seq=100

    //관리자 송금하기
    @GetMapping("/image/getSellerAccount")
    public  @ResponseBody  ResponseEntity<Map<String,Object>> getSellerImageAccount(@RequestParam("tradingid") Long tradingid, Model model){
        Map<String,Object> result=paymentImageService.getSellerAccount(tradingid);
        return new ResponseEntity(result,HttpStatus.OK);
    }
    //관리자 송금하기
    @GetMapping("/music/getSellerAccount")
    public @ResponseBody ResponseEntity<Map<String,Object>> getSellerMusicAccount(@RequestParam("tradingid") Long tradingid, Model model){
        Map<String,Object> result=paymentMusicService.getSellerAccount(tradingid);

        return new ResponseEntity(result,HttpStatus.OK);
    }
    //관리자 송금하기
    @GetMapping("/remittance/image")
    public   ResponseEntity<String> remittance(@RequestParam("tradingid") Long tradingid, Model model){
        boolean isRemittance=paymentImageService.remittanceUser(tradingid);
        return new ResponseEntity("SUCCESS",HttpStatus.OK);
    }
    //관리자 송금하기
    @GetMapping("/remittance/music")
    public ResponseEntity<String> remittance_music(@RequestParam("tradingid") Long tradingid, Model model){
        boolean isRemittance=paymentMusicService.remittanceUser(tradingid);
        return new ResponseEntity("SUCCESS",HttpStatus.OK);
    }
}
