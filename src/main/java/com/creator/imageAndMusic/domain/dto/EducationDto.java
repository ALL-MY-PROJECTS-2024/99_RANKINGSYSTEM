package com.creator.imageAndMusic.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationDto {
    private Long id;
    private String category;
    private String title;
    private String type;
    private String link;
    private MultipartFile file;
}
