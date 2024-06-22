package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.domain.dto.PaymentDto;
import com.creator.imageAndMusic.domain.service.PaymentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentServiceImpl paymentService;
    @PostMapping("/add")
    public ResponseEntity<String> payment_insert(@RequestBody PaymentDto paymentDto) throws IOException {
        log.info("/payment/add..." + paymentDto);
        paymentService.addPayment(paymentDto);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    //https://hyphen.im/product-api/view?seq=100

    //관리자 송금하기
    @GetMapping("/remittance")
    public ResponseEntity<String> remittance(@RequestParam("tradingid") Long tradingid, Model model){

        boolean isRemittance=paymentService.remittanceUser(tradingid);

        return new ResponseEntity("SUCCESS",HttpStatus.OK);
    }

}
