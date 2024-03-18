package com.example.demo.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pay_id;


    //IMPORT 결제 정보
    private String imp_uid;
    private String merchant_uid;
    private String pay_method;
    private String name;
    private String paid_amount;
    private String status;


    @ElementCollection
    private List<String> imageboard_id;



    //유저정보
    @ManyToOne
    @JoinColumn(name = "username",foreignKey = @ForeignKey(name="FK_payment_user_01",
            foreignKeyDefinition ="FOREIGN KEY(username) REFERENCES user(username) ON DELETE CASCADE ON UPDATE CASCADE" ))
    private User user;


    //주소지
    private String address;
    //등록날짜
    private LocalDateTime regdate;




}
