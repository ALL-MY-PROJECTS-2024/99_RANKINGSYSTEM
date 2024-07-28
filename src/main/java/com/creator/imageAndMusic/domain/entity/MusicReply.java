package com.creator.imageAndMusic.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MusicReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "musicid", foreignKey = @ForeignKey(name="FK_MusicReply_Music_Read_1",
            foreignKeyDefinition ="FOREIGN KEY(musicid) REFERENCES music(musicid) ON DELETE CASCADE ON UPDATE CASCADE" ))
    private Music music;

    @ManyToOne
    @JoinColumn(name = "username",foreignKey = @ForeignKey(name="FK_MusicReply_User_Read_1",
            foreignKeyDefinition ="FOREIGN KEY(username) REFERENCES user(username) ON DELETE CASCADE ON UPDATE CASCADE" ))
    private User user;

    private LocalDateTime date;

    private String context;


}
