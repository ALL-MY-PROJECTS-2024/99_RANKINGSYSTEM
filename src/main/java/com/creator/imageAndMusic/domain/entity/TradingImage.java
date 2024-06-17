package com.creator.imageAndMusic.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TradingImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long tradingid;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller",foreignKey = @ForeignKey(name = "FK_user_trading_seller",
            foreignKeyDefinition = "FOREIGN KEY (seller) REFERENCES user(username) ON DELETE CASCADE ON UPDATE CASCADE") ) //FK설정\
    User seller ;//판매자유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer",foreignKey = @ForeignKey(name = "FK_user_trading_buyer",
            foreignKeyDefinition = "FOREIGN KEY (buyer) REFERENCES user(username) ON DELETE CASCADE ON UPDATE CASCADE") ) //FK설정\
    User buyer;//구매자유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fileid",foreignKey = @ForeignKey(name = "FK_imagesFileInfo_Tading_img",
            foreignKeyDefinition = "FOREIGN KEY (fileid) REFERENCES images_file_info(fileid) ON DELETE CASCADE ON UPDATE CASCADE") ) //FK설정\
    ImagesFileInfo fileid;      //이미지 파일(랭킹에 등록되어있는 - 조회수 5회이상, 좋아요 3이상 - 조건셋업은 나중에)

    boolean isAdminAccepted;            //관리자 경매 승인 여부
    String auctionState;                //경매상태
    LocalDateTime reqStartTime ;        //경매요청일/시간
    LocalDateTime reqTimeout ;          //경매미승인시 자동 삭제시간
    LocalDateTime auctionStartTime ;    //경매시작일/시간
    LocalDateTime auctionEndTime;       //경매종료일/시간
    String price;                       //낙찰가

    boolean paymentState; //            //결제여부

    //채팅방
    String roomId;
    Long max;
    @ElementCollection
    List<String> members;







}
