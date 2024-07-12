package com.creator.imageAndMusic.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class MusicFileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileid;

    @ManyToOne
    @JoinColumn(name = "musicid",foreignKey = @ForeignKey(name="FK_Music_musicfileinfo",
            foreignKeyDefinition ="FOREIGN KEY(musicid) REFERENCES music(musicid) ON DELETE CASCADE ON UPDATE CASCADE" ))
    private Music music;
    private String dir;
    private String filename;
    private String albumImageName;


}
