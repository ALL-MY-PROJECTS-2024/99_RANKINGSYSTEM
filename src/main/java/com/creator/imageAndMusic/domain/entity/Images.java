package com.creator.imageAndMusic.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iamgeid;

    private String username;
    private String title;
    private String mainCategory;
    private String subCategory;
    private String description;
    private LocalDateTime createAt;
    private double lat;
    private double lng;


}
