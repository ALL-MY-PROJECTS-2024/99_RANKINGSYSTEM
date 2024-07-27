package com.creator.imageAndMusic.domain.repository;


import com.creator.imageAndMusic.domain.entity.ImageReply;
import com.creator.imageAndMusic.domain.entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageReplyRepository extends JpaRepository<ImageReply,Long> {
    List<ImageReply> findAllByImage(Images images);

    List<ImageReply> findAllByImageOrderByIdDesc(Images images);
}
