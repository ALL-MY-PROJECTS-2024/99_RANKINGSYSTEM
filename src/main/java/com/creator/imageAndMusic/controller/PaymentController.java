package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.domain.dto.PaymentDto;
import com.creator.imageAndMusic.domain.service.PaymentImageServiceImpl;
import com.creator.imageAndMusic.domain.service.PaymentMusicServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "결제", description = "결제 처리관련 기능")
public class PaymentController {

    @Autowired
    private PaymentMusicServiceImpl paymentMusicService;
    @Autowired
    private PaymentImageServiceImpl paymentImageService;



    @Operation(
            summary = "홈>내 이미지>내정보>낙찰받은 경매",
            description = "낙찰 완료 이후 결제 처리",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @PostMapping("/add/image")
    public ResponseEntity<String> payment_insert(@RequestBody PaymentDto paymentDto) throws IOException {
        log.info("/payment/add/image..." + paymentDto);
        paymentImageService.addPayment(paymentDto);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @Operation(
            summary = "홈>내 이미지>내정보>낙찰받은 경매",
            description = "낙찰 완료 이후 결제 처리",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @PostMapping("/add/music")
    public ResponseEntity<String> payment_insert_music(@RequestBody PaymentDto paymentDto) throws IOException {
        log.info("/add/music..." + paymentDto);
        paymentMusicService.addPayment(paymentDto);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
    //https://hyphen.im/product-api/view?seq=100

    @Operation(
            summary = "홈>매매>이미지",
            description = "관리자 송금위한 계좌정보 확인",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    //관리자 송금하기
    @GetMapping("/image/getSellerAccount")
    public  @ResponseBody  ResponseEntity<Map<String,Object>> getSellerImageAccount(@RequestParam("tradingid") Long tradingid, Model model){
        Map<String,Object> result=paymentImageService.getSellerAccount(tradingid);
        return new ResponseEntity(result,HttpStatus.OK);
    }

    @Operation(
            summary = "홈>매매>음악",
            description = "관리자 송금위한 계좌정보 확인",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    //관리자 송금하기
    @GetMapping("/music/getSellerAccount")
    public @ResponseBody ResponseEntity<Map<String,Object>> getSellerMusicAccount(@RequestParam("tradingid") Long tradingid, Model model){
        Map<String,Object> result=paymentMusicService.getSellerAccount(tradingid);

        return new ResponseEntity(result,HttpStatus.OK);
    }


    @Operation(
            summary = "홈>매매>이미지",
            description = "관리자 송금위한 계좌송금 처리",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/remittance/image")
    public   ResponseEntity<String> remittance(@RequestParam("tradingid") Long tradingid, Model model){
        boolean isRemittance=paymentImageService.remittanceUser(tradingid);
        return new ResponseEntity("SUCCESS",HttpStatus.OK);
    }

    @Operation(
            summary = "홈>매매>음악",
            description = "관리자 송금위한 계좌송금 처리",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    //관리자 송금하기
    @GetMapping("/remittance/music")
    public ResponseEntity<String> remittance_music(@RequestParam("tradingid") Long tradingid, Model model){
        boolean isRemittance=paymentMusicService.remittanceUser(tradingid);
        return new ResponseEntity("SUCCESS",HttpStatus.OK);
    }
}
