package com.creator.imageAndMusic.domain.repository;


import com.creator.imageAndMusic.domain.entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagesRepository extends JpaRepository<Images,Long> {

    List<Images> findAllByUsername(String username);

}
