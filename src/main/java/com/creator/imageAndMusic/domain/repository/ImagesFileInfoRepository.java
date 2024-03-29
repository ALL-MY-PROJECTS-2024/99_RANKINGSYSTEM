package com.creator.imageAndMusic.domain.repository;

import com.creator.imageAndMusic.domain.entity.Images;
import com.creator.imageAndMusic.domain.entity.ImagesFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagesFileInfoRepository extends JpaRepository<ImagesFileInfo,Long> {


    List<ImagesFileInfo> findAllByImages(Images images);



}
