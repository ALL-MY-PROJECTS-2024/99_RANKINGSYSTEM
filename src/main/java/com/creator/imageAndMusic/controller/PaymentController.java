package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.domain.dto.PaymentDto;
import com.creator.imageAndMusic.domain.service.PaymentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentServiceImpl paymentService;
    @PostMapping("/add")
    public ResponseEntity<String> payment_insert(@RequestBody PaymentDto paymentDto){
        log.info("/payment/add..." + paymentDto);
        paymentService.addPayment(paymentDto);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }


}
