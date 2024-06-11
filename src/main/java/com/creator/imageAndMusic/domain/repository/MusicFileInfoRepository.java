package com.creator.imageAndMusic.domain.repository;


import com.creator.imageAndMusic.domain.entity.MusicFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicFileInfoRepository extends JpaRepository<MusicFileInfo,Long> {
}
