package com.creator.imageAndMusic.domain.repository;


import com.creator.imageAndMusic.domain.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteMusicRepository extends JpaRepository<FavoriteMusic,Long> {

    boolean existsByUserAndMusicRanking(User user, MusicRanking musicRanking);

    int deleteByUserAndMusicRanking(User user,MusicRanking musicRanking);

    long countByMusicRanking(MusicRanking musicRanking);

    List<FavoriteMusic> findByUser(User user);

}
