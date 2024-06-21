package com.creator.imageAndMusic.domain.service;

import com.creator.imageAndMusic.domain.dto.PaymentDto;
import com.creator.imageAndMusic.domain.entity.Payment;
import com.creator.imageAndMusic.domain.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PaymentServiceImpl {

    @Autowired
    private PaymentRepository paymentRepository;

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

        paymentRepository.save(payment);
        
        //파일 옮기기 or Payment 폴더를 따로만들어서 저장

        //상태 여부 변경?

    }

}
