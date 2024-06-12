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
public class MusicRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rankingId;
    @ManyToOne
    @JoinColumn(name = "music_file_info",foreignKey = @ForeignKey(name="FK_Music_MusicRanking",
            foreignKeyDefinition ="FOREIGN KEY(music_file_info) REFERENCES music_file_info(fileid) ON DELETE CASCADE ON UPDATE CASCADE" ))
    private MusicFileInfo musicFileInfo;

    private Long count;
    private Long ilikeit;
    private LocalDateTime regDate;
}
