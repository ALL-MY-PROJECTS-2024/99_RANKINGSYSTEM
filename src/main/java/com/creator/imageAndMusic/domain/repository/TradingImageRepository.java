package com.creator.imageAndMusic.domain.repository;


import com.creator.imageAndMusic.domain.entity.ImagesFileInfo;
import com.creator.imageAndMusic.domain.entity.TradingImage;
import com.creator.imageAndMusic.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradingImageRepository extends JpaRepository<TradingImage, Long> {
    List<TradingImage> findAllBySeller(User seller);

    boolean existsBySellerAndFileid(User user , ImagesFileInfo imageFile);

    List<TradingImage> findAllByBuyer(User user);
}
