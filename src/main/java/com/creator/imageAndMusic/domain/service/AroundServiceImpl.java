package com.creator.imageAndMusic.domain.service;


import com.creator.imageAndMusic.domain.entity.ImagesFileInfo;
import com.creator.imageAndMusic.domain.repository.ImagesFileInfoRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AroundServiceImpl {
    @Autowired
    private ImagesFileInfoRepository imagesFileInfoRepository;

    @Transactional(rollbackFor=Exception.class)
    public List<ImagesFileInfo> getAllImages(){
           return  imagesFileInfoRepository.findAll();
    }

}
