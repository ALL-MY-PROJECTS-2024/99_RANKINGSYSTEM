package com.creator.imageAndMusic.domain.service;

import com.creator.imageAndMusic.domain.entity.ImagesRanking;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImageRankingService {

    boolean addRankingImage(Long fileid) throws Exception;

    List<ImagesRanking> getAllImageRanking();
}
