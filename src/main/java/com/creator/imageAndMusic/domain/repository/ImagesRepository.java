package com.creator.imageAndMusic.domain.repository;


import com.creator.imageAndMusic.domain.entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagesRepository extends JpaRepository<Images,Long> {

    List<Images> findAllByUsername(String username);

    void deleteAllByUsername(String username);


    @Query("SELECT i FROM Images i WHERE i.subCategory=:subcategory")
    List<Images> findAllBySubCategory(@Param("subcategory") String subcategory);

}
