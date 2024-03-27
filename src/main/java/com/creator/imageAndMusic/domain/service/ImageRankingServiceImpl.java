package com.creator.imageAndMusic.domain.service;

import com.creator.imageAndMusic.domain.entity.ImagesFileInfo;
import com.creator.imageAndMusic.domain.entity.ImagesRanking;
import com.creator.imageAndMusic.domain.repository.ImageRankingRepository;
import com.creator.imageAndMusic.domain.repository.ImagesFileInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
public class ImageRankingServiceImpl implements ImageRankingService {

    @Autowired
    private ImageRankingRepository imageRankingRepostiroy;
    @Autowired
    private ImagesFileInfoRepository imagesFileInfoRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addRankingImage(Long fileid) throws Exception {


        ImagesFileInfo imagesFileInfo =  imagesFileInfoRepository.findById(fileid).get();
        log.info("ImageRankingServiceImpl imagesFileInfo : ",imagesFileInfo);

        //ImagesFileInfo imagesFileInfo2 = imageRankingRepostiroy.findByImagesFileInfo(imagesFileInfo);
        //log.info("ImageRankingServiceImpl isNotNull : ",isNotnull);
        //.info("ImageRankingServiceImpl's is not null :"+(imagesFileInfo.getFileid()==imagesFileInfo2.getFileid()));

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
    public List<ImagesRanking> getAllImageRanking() {


        return imageRankingRepostiroy.findAllByOrderByCountDesc();
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
