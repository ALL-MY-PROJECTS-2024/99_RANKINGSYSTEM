package com.creator.imageAndMusic.domain.repository;


import com.creator.imageAndMusic.domain.entity.ImagesFileInfo;
import com.creator.imageAndMusic.domain.entity.ImagesRanking;
import com.creator.imageAndMusic.domain.entity.MusicFileInfo;
import com.creator.imageAndMusic.domain.entity.MusicRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface MusicRankingRepository extends JpaRepository<MusicRanking,Long> {
    @Query("SELECT ir FROM MusicRanking ir ORDER BY ir.count DESC")
    List<MusicRanking> findAllByOrderByCountDesc();

    @Query("SELECT ir FROM MusicRanking ir ORDER BY ir.ilikeit DESC")
    List<MusicRanking> findAllByOrderByLikeDesc();

    @Query(value = "SELECT * FROM music_ranking ORDER BY count DESC LIMIT :amount OFFSET :offset", nativeQuery = true)
    List<MusicRanking> findMusicRankingAmountStart(@Param("amount") int amount, @Param("offset") int offset);

    @Query(value = "SELECT * FROM music_ranking ORDER BY ilikeit DESC LIMIT :amount OFFSET :offset", nativeQuery = true)
    List<MusicRanking> findMusicRankingAmountStartOderByLike(@Param("amount") int amount, @Param("offset") int offset);

    MusicRanking findByMusicFileInfo(MusicFileInfo musicFileInfo);

    List<MusicRanking> findTop10ByOrderByCountDesc();

    List<MusicRanking> findTop10ByOrderByIlikeitDesc();



}
