package com.creator.imageAndMusic.domain.repository;

import com.creator.imageAndMusic.domain.entity.Board;
import com.creator.imageAndMusic.domain.entity.Images;
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

    @Query("SELECT ir FROM ImagesRanking ir ORDER BY ir.ilikeit DESC")
    List<ImagesRanking> findAllByOrderByLikeDesc();

    @Query(value = "SELECT * FROM images_ranking ORDER BY count DESC LIMIT :amount OFFSET :offset", nativeQuery = true)
    List<ImagesRanking> findImagesRankingAmountStart(@Param("amount") int amount, @Param("offset") int offset);

    @Query(value = "SELECT * FROM images_ranking ORDER BY ilikeit DESC LIMIT :amount OFFSET :offset", nativeQuery = true)
    List<ImagesRanking> findImagesRankingAmountStartOderByLike(@Param("amount") int amount, @Param("offset") int offset);

    ImagesRanking findByImagesFileInfo(ImagesFileInfo imagesFileInfo);

    List<ImagesRanking> findTop10ByOrderByCountDesc();

    List<ImagesRanking> findTop10ByOrderByIlikeitDesc();
    @Query(value =
            "SELECT ir.ranking_id,ir.count,ir.ilikeit,ir.reg_date,ir.images_file_info " +
            "FROM images_file_info ifi " +
            "INNER JOIN images i " +
            "ON ifi.iamgeid = i.iamgeid " +
            "INNER JOIN images_ranking ir " +
            "ON ir.images_file_info=ifi.fileid " +
            "WHERE i.sub_category = :subCategory " +
            "ORDER BY ir.ranking_id DESC " +
            "LIMIT :amount offset :offset"
            , nativeQuery = true //LIMIT 사용위해
    )
    List<ImagesRanking> findImageRankingAmountStart(@Param("subCategory") String subCategory, @Param("amount") int amount, @Param("offset") int offset);

    @Query(value =
            "SELECT count(ir.ranking_id) " +
                    "FROM images_file_info ifi " +
                    "INNER JOIN images i " +
                    "ON ifi.iamgeid = i.iamgeid " +
                    "INNER JOIN images_ranking ir " +
                    "ON ir.images_file_info=ifi.fileid " +
                    "WHERE i.sub_category = :subCategory "
            , nativeQuery = true //LIMIT 사용위해
    )
    int findImageRankingSubCategoryCount(@Param("subCategory") String subCategory);

}
