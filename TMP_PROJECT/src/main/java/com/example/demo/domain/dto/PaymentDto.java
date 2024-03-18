package com.example.demo.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaymentDto {
    private Long id;
    private String imp_uid;
    private String merchant_uid;
    private String pay_method;
    private String name;
    private String price;
    private String status;
    private String address;
    private List<String> cart_id;
}
