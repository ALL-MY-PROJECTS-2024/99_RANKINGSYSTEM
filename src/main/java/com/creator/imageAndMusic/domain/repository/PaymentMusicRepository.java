package com.creator.imageAndMusic.domain.repository;


import com.creator.imageAndMusic.domain.entity.PaymentMusic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMusicRepository extends JpaRepository<PaymentMusic,String> {
}
