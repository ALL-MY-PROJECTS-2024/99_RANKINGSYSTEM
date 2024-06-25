package com.creator.imageAndMusic.domain.repository;


import com.creator.imageAndMusic.domain.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradingMusicRepository extends JpaRepository<TradingMusic, Long> {
    List<TradingMusic> findAllBySeller(User seller);

    boolean existsBySellerAndFileid(User user , MusicFileInfo musicFile);

    List<TradingMusic> findAllByBuyer(User user);
}
