package com.creator.imageAndMusic.domain.service;

import com.creator.imageAndMusic.domain.dto.PaymentDto;
import com.creator.imageAndMusic.domain.entity.*;
import com.creator.imageAndMusic.domain.repository.*;
import com.creator.imageAndMusic.properties.UPLOADPATH;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class PaymentMusicServiceImpl {

    @Autowired
    private PaymentMusicRepository paymentMusicRepository;

    @Autowired
    private TradingMusicRepository tradingMusicRepository;

    @Autowired
    private MusicRepository musicRepository;
    @Autowired
    private MusicFileInfoRepository musicFileInfoRepository;


    @Transactional(rollbackFor = Exception.class)
    public void addPayment(PaymentDto paymentDto) throws IOException {
        PaymentMusic payment = new PaymentMusic();
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
        Optional<TradingMusic> tradingMusicOptional = tradingMusicRepository.findById(paymentDto.getTradingid());
        if(tradingMusicOptional.isEmpty())
            return ;

        TradingMusic tradingMusic = tradingMusicOptional.get();
        //이미지 정보 변경
        //이미지 파일 정보 변경

        //파일 옮기기 or Payment 폴더를 따로 만들어서 저장
        //판매자 경로
//        String oldPath= UPLOADPATH.ROOTDIRPATH+ File.separator+UPLOADPATH.UPPERDIRPATH+ File.separator;
//        oldPath+=UPLOADPATH.IMAGEDIRPATH+ File.separator+tradingImage.getSeller().getUsername()+ File.separator+tradingImage.getFileid().getImages().getSubCategory()+File.separator+tradingImage.getFileid().getImages().getIamgeid();

        String oldPath = UPLOADPATH.ROOTDIRPATH+File.separator +  tradingMusic.getFileid().getDir() + File.separator ;
        //구매자 경로
        String newPath= UPLOADPATH.ROOTDIRPATH+ File.separator+UPLOADPATH.UPPERDIRPATH+ File.separator;
        newPath+=UPLOADPATH.IMAGEDIRPATH+ File.separator+tradingMusic.getBuyer().getUsername()+ File.separator+"payment"+File.separator +  tradingMusic.getFileid().getMusic().getSubCategory()+File.separator+tradingMusic.getFileid().getMusic().getMusicid();
        File dir = new File(newPath);
        System.out.println("NEWPATH :  " +newPath);
        if(!dir.exists())
            dir.mkdirs();

        //파일 복사
        String oldFilePath = oldPath + File.separator + tradingMusic.getFileid().getFilename();
        String newFilePath = newPath + File.separator +"a_"+tradingMusic.getFileid().getFilename();

        File in = new File(oldFilePath);
        File out =new File(newFilePath);

        FileCopyUtils.copy(in,out);
        
        //판매자 파일삭제
        in.delete();

        //파일 소유권 이전
        MusicFileInfo musicFileInfo = tradingMusic.getFileid();
        Music music =  musicFileInfo.getMusic();
        music.setUsername(tradingMusic.getBuyer().getUsername());

        musicRepository.save(music);
        newPath= File.separator+UPLOADPATH.UPPERDIRPATH+ File.separator;
        newPath+=UPLOADPATH.IMAGEDIRPATH+ File.separator+tradingMusic.getBuyer().getUsername()+ File.separator+"payment"+File.separator +  tradingMusic.getFileid().getMusic().getSubCategory()+File.separator+tradingMusic.getFileid().getMusic().getMusicid();
        musicFileInfo.setDir(newPath);
        musicFileInfo.setFilename("a_"+tradingMusic.getFileid().getFilename());
        musicFileInfo.setMusic(music);
        musicFileInfoRepository.save(musicFileInfo);
        tradingMusic.setFileid(musicFileInfo);

        //tradingImage 상태 여부 변경 - 결제 완료
        tradingMusic.setStatus("결제완료");
        tradingMusicRepository.save(tradingMusic);

        payment.setItem_id(tradingMusic.getFileid());
        paymentMusicRepository.save(payment);

        //판매완료는 판매자가  송금요청 할때
        //송금처리후 파일 삭제


    }
    @Transactional(rollbackFor = Exception.class)

    public boolean remittanceUser(Long tradingid) {
        //송금 처리 하기 API 필요

        Optional<TradingMusic> tradingMusicOptional =  tradingMusicRepository.findById(tradingid);
        if(tradingMusicOptional.isEmpty())
            return false;
        TradingMusic tradingMusic = tradingMusicOptional.get();
        tradingMusic.setStatus("송금완료");
        tradingMusicRepository.save(tradingMusic);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> getSellerAccount(Long tradingid) {
        Optional<TradingMusic> tradingMusicOptional = tradingMusicRepository.findById(tradingid);
        if(tradingMusicOptional.isEmpty())
            return null;
        Map<String,Object> result = new LinkedHashMap<String,Object>();
        TradingMusic tradingMusic = tradingMusicOptional.get();
        result.put("bankname",tradingMusic.getSeller().getBankname());
        result.put("account",tradingMusic.getSeller().getAccount());
        return result;
    }
}
