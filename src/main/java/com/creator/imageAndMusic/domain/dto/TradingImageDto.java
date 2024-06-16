package com.creator.imageAndMusic.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradingImageDto {

    Long tradingid;
    String seller ;                     //판매자유저
    String buyer;                       //구매자유저
    Long fileid;                        //이미지 파일(랭킹에 등록되어있는 - 조회수 5회이상, 좋아요 3이상 - 조건셋업은 나중에)
    String title;
    boolean isAdminAccepted;            //관리자 경매 승인 여부
    String auctionState;                //경매상태
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime auctionStartTime ;    //경매시작일/시간
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime auctionEndTime;       //경매종료일/시간
    String price;                       //낙찰가
    boolean paymentState; //            //결제여부


    public void  setAdminAccepted(boolean adminAccepted){
        this.isAdminAccepted = adminAccepted;
    }
    public boolean getAdminAccepted(){
       return  this.isAdminAccepted;
    }
}
