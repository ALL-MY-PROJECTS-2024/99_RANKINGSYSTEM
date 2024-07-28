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

    @Query(value =
            "SELECT ir.ranking_id,ir.count,ir.ilikeit,ir.reg_date,ir.music_file_info " +
                    "FROM music_File_info ifi " +
                    "INNER JOIN music i " +
                    "ON ifi.musicid = i.musicid " +
                    "INNER JOIN music_ranking ir " +
                    "ON ir.music_file_info=ifi.fileid " +
                    "WHERE i.sub_category = :subCategory " +
                    "ORDER BY ir.ranking_id DESC " +
                    "LIMIT :amount offset :offset"
            , nativeQuery = true //LIMIT 사용위해
    )
    List<MusicRanking> findMusicRankingAmountStart(@Param("subCategory") String subCategory, @Param("amount") int amount, @Param("offset") int offset);

    @Query(value =
            "SELECT count(ir.ranking_id) " +
                    "FROM music_File_info ifi " +
                    "INNER JOIN music i " +
                    "ON ifi.musicid = i.musicid " +
                    "INNER JOIN music_ranking ir " +
                    "ON ir.music_file_info=ifi.fileid " +
                    "WHERE i.sub_category = :subCategory "
            , nativeQuery = true //LIMIT 사용위해
    )
    int findMusicRankingSubCategoryCount(@Param("subCategory") String subCategory);


}
