package com.creator.imageAndMusic.domain.service;

import com.creator.imageAndMusic.domain.dto.Criteria;
import com.creator.imageAndMusic.domain.entity.ImagesFileInfo;
import com.creator.imageAndMusic.domain.entity.ImagesRanking;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ImageRankingService {

    boolean addRankingImage(Long fileid) throws Exception;

    Map<String,Object> getAllImageRanking(Criteria criteria);

    @Transactional(rollbackFor = Exception.class)
    List<ImagesRanking> getAllImageRanking();

    @Transactional(rollbackFor = SQLException.class)
    void count(Long rankingId);

    ImagesRanking getImageRanking(Long rankingId);

    //지역별로 묶인 이미지 정보가져오기(USER가 Ranking등록한 이미지 가져와서 지역별로 선별하기)
    @Transactional(rollbackFor = SQLException.class)
    Map<String,Object> getLocalImageRanking();

    @Transactional(rollbackFor = SQLException.class)
    List<ImagesFileInfo> getAllImageRankingByCategory(String subCategory);
}
