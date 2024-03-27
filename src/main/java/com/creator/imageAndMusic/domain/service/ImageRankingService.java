package com.creator.imageAndMusic.domain.service;

import com.creator.imageAndMusic.domain.dto.Criteria;
import com.creator.imageAndMusic.domain.entity.ImagesRanking;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ImageRankingService {

    boolean addRankingImage(Long fileid) throws Exception;

    Map<String,Object> getAllImageRanking(Criteria criteria);

    @Transactional(rollbackFor = SQLException.class)
    void count(Long rankingId);

    ImagesRanking getImageRanking(Long rankingId);
}
