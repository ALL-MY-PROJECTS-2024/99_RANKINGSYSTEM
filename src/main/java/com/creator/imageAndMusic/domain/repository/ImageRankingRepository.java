package com.creator.imageAndMusic.domain.repository;

import com.creator.imageAndMusic.domain.entity.ImagesRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRankingRepository extends JpaRepository<ImagesRanking,Long> {


    //ImagesFileInfo findByImagesFileInfo(ImagesFileInfo imagesFileInfo);



}
