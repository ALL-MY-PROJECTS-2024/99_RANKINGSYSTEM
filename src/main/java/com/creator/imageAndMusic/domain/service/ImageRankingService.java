package com.creator.imageAndMusic.domain.service;

import org.springframework.transaction.annotation.Transactional;

public interface ImageRankingService {

    boolean addRankingImage(Long fileid) throws Exception;
}
