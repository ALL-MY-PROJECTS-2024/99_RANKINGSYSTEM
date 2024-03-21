package com.creator.imageAndMusic.domain.repository;

import com.creator.imageAndMusic.domain.entity.ImagesFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagesFileInfoRepository extends JpaRepository<ImagesFileInfo,Long> {

}
