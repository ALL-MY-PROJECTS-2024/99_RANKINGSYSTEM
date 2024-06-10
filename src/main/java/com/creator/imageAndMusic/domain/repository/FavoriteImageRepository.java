package com.creator.imageAndMusic.domain.repository;


import com.creator.imageAndMusic.domain.entity.FavoriteImage;
import com.creator.imageAndMusic.domain.entity.Images;
import com.creator.imageAndMusic.domain.entity.ImagesRanking;
import com.creator.imageAndMusic.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteImageRepository extends JpaRepository<FavoriteImage,Long> {

    boolean existsByUserAndImagesRanking(User user,ImagesRanking imagesRanking);

    int deleteByUserAndImagesRanking(User user,ImagesRanking imagesRanking);

    long countByImagesRanking(ImagesRanking imagesRanking);

    List<FavoriteImage> findByUser(User user);

}
