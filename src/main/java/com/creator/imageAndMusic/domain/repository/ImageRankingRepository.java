package com.creator.imageAndMusic.domain.repository;

import com.creator.imageAndMusic.domain.entity.Board;
import com.creator.imageAndMusic.domain.entity.ImagesFileInfo;
import com.creator.imageAndMusic.domain.entity.ImagesRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRankingRepository extends JpaRepository<ImagesRanking,Long> {

    @Query("SELECT ir FROM ImagesRanking ir ORDER BY ir.count DESC")
    List<ImagesRanking> findAllByOrderByCountDesc();

    @Query(value = "SELECT * FROM images_ranking ORDER BY count DESC LIMIT :amount OFFSET :offset", nativeQuery = true)
    List<ImagesRanking> findImagesRankingAmountStart(@Param("amount") int amount, @Param("offset") int offset);

    ImagesRanking findByImagesFileInfo(ImagesFileInfo imagesFileInfo);
}
