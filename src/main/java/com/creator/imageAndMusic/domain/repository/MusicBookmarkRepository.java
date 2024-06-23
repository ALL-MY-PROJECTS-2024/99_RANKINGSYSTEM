package com.creator.imageAndMusic.domain.repository;


import com.creator.imageAndMusic.domain.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicBookmarkRepository extends JpaRepository<MusicBookmark,Long> {
    boolean existsByMusicRanking(MusicRanking musicRanking);

//    @Query("SELECT b FROM Bookmark b ORDER BY b.id DESC")
//    List<Bookmark> findAllByOrderByIdDesc();

    List<MusicBookmark> findAllByUserOrderByIdDesc(User user);

}
