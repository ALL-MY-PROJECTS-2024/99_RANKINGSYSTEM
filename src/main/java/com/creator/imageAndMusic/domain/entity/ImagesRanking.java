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
public class ImagesRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rankingId;
    @ManyToOne
    @JoinColumn(name = "images_file_info",foreignKey = @ForeignKey(name="FK_Images_ImageRanking",
            foreignKeyDefinition ="FOREIGN KEY(images_file_info) REFERENCES images_file_info(fileid) ON DELETE CASCADE ON UPDATE CASCADE" ))
    private ImagesFileInfo imagesFileInfo;

    private Long count;
    private Long ilikeit;
    private LocalDateTime regDate;
}
