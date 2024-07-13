package com.creator.imageAndMusic.domain.repository;


import com.creator.imageAndMusic.domain.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<Education,Long> {

    List<Education> findAllByCategory(String dalle);
}
