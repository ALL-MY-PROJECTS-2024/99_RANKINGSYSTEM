package com.creator.imageAndMusic.domain.repository;


import com.creator.imageAndMusic.domain.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicRepository extends JpaRepository<Music,Long> {
}
