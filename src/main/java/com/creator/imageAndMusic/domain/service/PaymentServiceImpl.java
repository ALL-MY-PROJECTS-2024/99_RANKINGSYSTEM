package com.creator.imageAndMusic.domain.service;

import com.creator.imageAndMusic.domain.dto.PaymentDto;
import com.creator.imageAndMusic.domain.entity.Payment;
import com.creator.imageAndMusic.domain.entity.TradingImage;
import com.creator.imageAndMusic.domain.repository.ImagesFileInfoRepository;
import com.creator.imageAndMusic.domain.repository.ImagesRepository;
import com.creator.imageAndMusic.domain.repository.PaymentRepository;
import com.creator.imageAndMusic.domain.repository.TradingImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class PaymentServiceImpl {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TradingImageRepository tradingImageRepository;

    @Autowired
    private ImagesRepository imagesRepository;

    @Autowired
    private ImagesFileInfoRepository imagesFileInfoRepository;


    @Transactional(rollbackFor = Exception.class)
    public void addPayment(PaymentDto paymentDto){
        Payment payment = new Payment();
        payment.setSuccess(paymentDto.isSuccess());
        payment.setImp_uid(paymentDto.getImp_uid());
        payment.setPay_method(paymentDto.getPay_method());
        payment.setMerchant_uid(paymentDto.getMerchant_uid());
        payment.setName(paymentDto.getName());
        payment.setPaid_amount(paymentDto.getPaid_amount());
        payment.setCurrency(paymentDto.getCurrency());
        payment.setPg_provider(paymentDto.getPg_provider());
        payment.setPg_type(paymentDto.getPg_type());
        payment.setPg_tid(paymentDto.getPg_tid());
        payment.setApply_num(paymentDto.getApply_num());
        payment.setBuyer_name(paymentDto.getBuyer_name());
        payment.setBuyer_email(paymentDto.getBuyer_email());
        payment.setBuyer_tel(paymentDto.getBuyer_tel());
        payment.setBuyer_addr(paymentDto.getBuyer_addr());
        payment.setBuyer_postcode(paymentDto.getBuyer_postcode());
        payment.setStatus(paymentDto.getStatus());
        payment.setPaid_at(paymentDto.getPaid_at());
        payment.setReceipt_url(payment.getReceipt_url());
        payment.setCard_name(payment.getCard_name());
        payment.setBank_name(payment.getBank_name());
        payment.setCard_quota(payment.getCard_quota());
        payment.setCard_number(payment.getCard_number());

        //ImageFileInfo의 ID를 넣어야함
        Optional<TradingImage> tradingImageOptional = tradingImageRepository.findById(paymentDto.getTradingid());
        if(tradingImageOptional.isEmpty())
            return ;

        TradingImage tradingImage = tradingImageOptional.get();
        payment.setItem_id(tradingImage.getFileid());

        paymentRepository.save(payment);


        //이미지 정보 변경
        //이미지 파일 정보 변경

        //파일 옮기기 or Payment 폴더를 따로 만들어서 저장



        //tradingImage 상태 여부 변경 - 결제 완료
        tradingImage.setStatus("결제완료");
        tradingImageRepository.save(tradingImage);

        //판매완료는 판매자가  송금할때


    }

}
