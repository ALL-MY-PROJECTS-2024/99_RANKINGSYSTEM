package com.example.demo.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CartDto {
    private Long cart_id;
    private Long product_id;
    private int amount;
    private String color;
    private String username;
    private LocalDateTime reg_date;
}
