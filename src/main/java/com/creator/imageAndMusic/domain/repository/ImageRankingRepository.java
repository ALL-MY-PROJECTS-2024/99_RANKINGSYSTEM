package com.creator.imageAndMusic.domain.repository;

import com.creator.imageAndMusic.domain.entity.ImagesRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRankingRepository extends JpaRepository<ImagesRanking,Long> {

    @Query("SELECT ir FROM ImagesRanking ir ORDER BY ir.count DESC")
    List<ImagesRanking> findAllByOrderByCountDesc();

}
