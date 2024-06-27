package com.creator.imageAndMusic.domain.service;

import com.creator.imageAndMusic.domain.dto.PaymentDto;
import com.creator.imageAndMusic.domain.entity.Images;
import com.creator.imageAndMusic.domain.entity.ImagesFileInfo;
import com.creator.imageAndMusic.domain.entity.PaymentImage;
import com.creator.imageAndMusic.domain.entity.TradingImage;
import com.creator.imageAndMusic.domain.repository.ImagesFileInfoRepository;
import com.creator.imageAndMusic.domain.repository.ImagesRepository;
import com.creator.imageAndMusic.domain.repository.PaymentRepository;
import com.creator.imageAndMusic.domain.repository.TradingImageRepository;
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
public class PaymentImageServiceImpl {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TradingImageRepository tradingImageRepository;

    @Autowired
    private ImagesRepository imagesRepository;

    @Autowired
    private ImagesFileInfoRepository imagesFileInfoRepository;


    @Transactional(rollbackFor = Exception.class)
    public void addPayment(PaymentDto paymentDto) throws IOException {
        PaymentImage payment = new PaymentImage();
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
        //이미지 정보 변경
        //이미지 파일 정보 변경

        //파일 옮기기 or Payment 폴더를 따로 만들어서 저장
        //판매자 경로
//        String oldPath= UPLOADPATH.ROOTDIRPATH+ File.separator+UPLOADPATH.UPPERDIRPATH+ File.separator;
//        oldPath+=UPLOADPATH.IMAGEDIRPATH+ File.separator+tradingImage.getSeller().getUsername()+ File.separator+tradingImage.getFileid().getImages().getSubCategory()+File.separator+tradingImage.getFileid().getImages().getIamgeid();

        String oldPath = UPLOADPATH.ROOTDIRPATH+File.separator +  tradingImage.getFileid().getDir() + File.separator ;
        //구매자 경로
        String newPath= UPLOADPATH.ROOTDIRPATH+ File.separator+UPLOADPATH.UPPERDIRPATH+ File.separator;
        newPath+=UPLOADPATH.IMAGEDIRPATH+ File.separator+tradingImage.getBuyer().getUsername()+ File.separator+"payment"+File.separator +  tradingImage.getFileid().getImages().getSubCategory()+File.separator+tradingImage.getFileid().getImages().getIamgeid();
        File dir = new File(newPath);
        System.out.println("NEWPATH :  " +newPath);
        if(!dir.exists())
            dir.mkdirs();

        //파일 복사
        String oldFilePath = oldPath + File.separator + tradingImage.getFileid().getFilename();
        String newFilePath = newPath + File.separator + "a_"+tradingImage.getFileid().getFilename();

        File in = new File(oldFilePath);
        File out =new File(newFilePath);

        FileCopyUtils.copy(in,out);
        
        //섬네일 복사
        String oldFile_thumbnal_Path = oldPath + File.separator + "s_"+tradingImage.getFileid().getFilename();
        String newFile_thumbnal_Path = newPath + File.separator + "s_a_"+tradingImage.getFileid().getFilename();
        File in_thumb = new File(oldFile_thumbnal_Path);
        File out_thumb =new File(newFile_thumbnal_Path);

        FileCopyUtils.copy(in_thumb,out_thumb);

        //판매자 파일삭제
        in.delete();
        in_thumb.delete();


        //파일 소유권 이전
        ImagesFileInfo imagesFileInfo = tradingImage.getFileid();
        Images images =  imagesFileInfo.getImages();
        images.setUsername(tradingImage.getBuyer().getUsername());

        imagesRepository.save(images);
        newPath= File.separator+UPLOADPATH.UPPERDIRPATH+ File.separator;
        newPath+=UPLOADPATH.IMAGEDIRPATH+ File.separator+tradingImage.getBuyer().getUsername()+ File.separator+"payment"+File.separator +  tradingImage.getFileid().getImages().getSubCategory()+File.separator+tradingImage.getFileid().getImages().getIamgeid();
        imagesFileInfo.setDir(newPath);
        imagesFileInfo.setFilename("a_"+tradingImage.getFileid().getFilename());
        imagesFileInfo.setImages(images);
        imagesFileInfoRepository.save(imagesFileInfo);
        tradingImage.setFileid(imagesFileInfo);

        //tradingImage 상태 여부 변경 - 결제 완료
        tradingImage.setStatus("결제완료");
        tradingImageRepository.save(tradingImage);

        payment.setItem_id(tradingImage.getFileid());
        paymentRepository.save(payment);

        //판매완료는 판매자가  송금요청 할때
        //송금처리후 파일 삭제


    }
    @Transactional(rollbackFor = Exception.class)

    public boolean remittanceUser(Long tradingid) {
        //송금 처리 하기 API 필요

        Optional<TradingImage> tradingImageOptional =  tradingImageRepository.findById(tradingid);
        if(tradingImageOptional.isEmpty())
            return false;
        TradingImage tradingImage = tradingImageOptional.get();
        tradingImage.setStatus("송금완료");
        tradingImageRepository.save(tradingImage);

        return true;
    }
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> getSellerAccount(Long tradingid) {
        Optional<TradingImage> tradingImageOptional = tradingImageRepository.findById(tradingid);
        if(tradingImageOptional.isEmpty())
            return null;
        Map<String,Object> result = new LinkedHashMap<String,Object>();
        TradingImage tradingImage = tradingImageOptional.get();
        result.put("bankname",tradingImage.getSeller().getBankname());
        result.put("account",tradingImage.getSeller().getAccount());
        return result;
    }

}
