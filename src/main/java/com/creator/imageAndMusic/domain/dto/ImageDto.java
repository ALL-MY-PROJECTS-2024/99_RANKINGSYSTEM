package com.creator.imageAndMusic.domain.dto;

import jakarta.persistence.Entity;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageDto {
    private Long id;
    private String username;
    private String title;
    private String mainCategory;
    private String subCategory;
    private String description;
    private MultipartFile[] files;
}
