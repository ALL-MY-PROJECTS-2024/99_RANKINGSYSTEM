package com.creator.imageAndMusic.domain.repository;


import com.creator.imageAndMusic.domain.entity.Images;
import com.creator.imageAndMusic.domain.entity.ImagesFileInfo;
import com.creator.imageAndMusic.domain.entity.Music;
import com.creator.imageAndMusic.domain.entity.MusicFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicFileInfoRepository extends JpaRepository<MusicFileInfo,Long> {

    List<MusicFileInfo> findAllByMusic(Music music);


}
