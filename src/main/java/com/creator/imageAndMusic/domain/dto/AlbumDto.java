package com.creator.imageAndMusic.domain.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AlbumDto {
    private Long id;
    private String username;
    private String title;
    private String mainCategory;
    private String subCategory;
    private String description;
    private String tool;
    private MultipartFile[] files;
    private int count;
    private int like;
    private double lat;
    private double lng;
    private MultipartFile imageFile;

}
