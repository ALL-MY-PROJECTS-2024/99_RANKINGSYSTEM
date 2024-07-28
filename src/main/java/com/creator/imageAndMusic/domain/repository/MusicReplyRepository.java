package com.creator.imageAndMusic.domain.repository;

import com.creator.imageAndMusic.domain.entity.Music;
import com.creator.imageAndMusic.domain.entity.MusicReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicReplyRepository  extends JpaRepository<MusicReply,Long> {
    List<MusicReply> findAllByMusicOrderByIdDesc(Music music);
}
