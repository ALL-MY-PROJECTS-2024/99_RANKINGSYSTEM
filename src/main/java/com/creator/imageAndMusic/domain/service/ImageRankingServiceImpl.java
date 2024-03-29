package com.creator.imageAndMusic.domain.service;

import com.creator.imageAndMusic.domain.dto.Criteria;
import com.creator.imageAndMusic.domain.dto.PageDto;
import com.creator.imageAndMusic.domain.entity.Board;
import com.creator.imageAndMusic.domain.entity.ImagesFileInfo;
import com.creator.imageAndMusic.domain.entity.ImagesRanking;
import com.creator.imageAndMusic.domain.repository.ImageRankingRepository;
import com.creator.imageAndMusic.domain.repository.ImagesFileInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class ImageRankingServiceImpl implements ImageRankingService {


    @Autowired
    ApplicationContext appContext;

    @Autowired
    private ImageRankingRepository imageRankingRepostiroy;
    @Autowired
    private ImagesFileInfoRepository imagesFileInfoRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addRankingImage(Long fileid) throws Exception {

        if(fileid==null)
            return false;
        ImagesFileInfo imagesFileInfo =  imagesFileInfoRepository.findById(fileid).get();
        log.info("ImageRankingServiceImpl imagesFileInfo : ",imagesFileInfo);

        //ImagesFileInfo imagesFileInfo2 = imageRankingRepostiroy.findByImagesFileInfo(imagesFileInfo);
        //log.info("ImageRankingServiceImpl isNotNull : ",isNotnull);
        //.info("ImageRankingServiceImpl's is not null :"+(imagesFileInfo.getFileid()==imagesFileInfo2.getFileid()));

        ImagesRanking isExistedRnaking = imageRankingRepostiroy.findByImagesFileInfo(imagesFileInfo);
        if(isExistedRnaking!=null)
            return false;

        ImagesRanking imagesRanking = new ImagesRanking();
        imagesRanking.setRankingId(0L);
        imagesRanking.setImagesFileInfo(imagesFileInfo);
        imagesRanking.setCount(0L);
        imagesRanking.setIlikeit(0L);
        imagesRanking.setRegDate(LocalDateTime.now());

        imageRankingRepostiroy.save(imagesRanking);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> getAllImageRanking(Criteria criteria) {

        Map<String,Object> returns = new HashMap<String,Object>();

        //count
        int totalcount =(int)imageRankingRepostiroy.count();

        //PageDto 만들기
        PageDto pagedto = new PageDto(totalcount,criteria);
        //시작 게시물 번호 구하기(수정) - OFFSET
        int offset =(criteria.getPageno()-1) * criteria.getAmount();    //1page = 0, 2page = 10

        List<ImagesRanking> list  =  imageRankingRepostiroy.findImagesRankingAmountStart(pagedto.getCriteria().getAmount(),offset);

        List<ImagesRanking> rankingList =  imageRankingRepostiroy.findAllByOrderByCountDesc();

        returns.put("rankingList",rankingList);
        returns.put("list",list);
        returns.put("pageDto",pagedto);
        returns.put("count",totalcount);

        return returns;


    }


    @Override
    @Transactional(rollbackFor = SQLException.class)
    public void count(Long rankingId) {
        ImagesRanking imageRanking =  imageRankingRepostiroy.findById(rankingId).get();
        imageRanking.setCount(imageRanking.getCount()+1);
        imageRankingRepostiroy.save(imageRanking);
    }

    @Override
    @Transactional(rollbackFor = SQLException.class)
    public ImagesRanking getImageRanking(Long rankingId) {
       return imageRankingRepostiroy.findById(rankingId).get();
    }


}
