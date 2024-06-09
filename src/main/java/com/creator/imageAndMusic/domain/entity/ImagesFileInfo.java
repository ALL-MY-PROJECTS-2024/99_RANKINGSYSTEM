package com.creator.imageAndMusic.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class ImagesFileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileid;

    @ManyToOne
    @JoinColumn(name = "iamgeid",foreignKey = @ForeignKey(name="FK_Images_imagesfileInfo",
            foreignKeyDefinition ="FOREIGN KEY(iamgeid) REFERENCES images(iamgeid) ON DELETE CASCADE ON UPDATE CASCADE" ))
    private Images images;
    private String dir;
    private String filename;
}
