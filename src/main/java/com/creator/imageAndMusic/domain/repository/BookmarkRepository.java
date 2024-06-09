package com.creator.imageAndMusic.domain.repository;


import com.creator.imageAndMusic.domain.entity.Bookmark;
import com.creator.imageAndMusic.domain.entity.ImagesRanking;
import com.creator.imageAndMusic.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
    boolean existsByImagesRanking(ImagesRanking imagesRanking);

//    @Query("SELECT b FROM Bookmark b ORDER BY b.id DESC")
//    List<Bookmark> findAllByOrderByIdDesc();

    List<Bookmark> findAllByUserOrderByIdDesc(User user);
}
