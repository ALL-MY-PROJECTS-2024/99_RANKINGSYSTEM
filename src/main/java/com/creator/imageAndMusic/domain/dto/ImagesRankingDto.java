package com.creator.imageAndMusic.domain.dto;

import com.creator.imageAndMusic.domain.entity.ImagesFileInfo;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ImagesRankingDto {

    private Long rankingId;
    private ImagesFileInfo imagesFileInfo;
    private Long count;
    private Long ilikeit;
    private LocalDateTime regDate;
}
