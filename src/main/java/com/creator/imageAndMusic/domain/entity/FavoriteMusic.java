package com.creator.imageAndMusic.domain.entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FavoriteMusic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ranking_id",foreignKey = @ForeignKey(name="FK_Music_FavoriteMusic",
            foreignKeyDefinition ="FOREIGN KEY(ranking_id) REFERENCES music_ranking(ranking_id) ON DELETE CASCADE ON UPDATE CASCADE" ))
    private MusicRanking musicRanking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username",foreignKey = @ForeignKey(name="FK_User_FavoriteMusic",
            foreignKeyDefinition ="FOREIGN KEY(username) REFERENCES user(username) ON DELETE CASCADE ON UPDATE CASCADE" ))
    private User user;
}
