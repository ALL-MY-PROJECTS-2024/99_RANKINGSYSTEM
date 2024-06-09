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
public class FavoriteImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ranking_id",foreignKey = @ForeignKey(name="FK_Images_FavoriteImage",
            foreignKeyDefinition ="FOREIGN KEY(ranking_id) REFERENCES images_ranking(ranking_id) ON DELETE CASCADE ON UPDATE CASCADE" ))
    private ImagesRanking imagesRanking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username",foreignKey = @ForeignKey(name="FK_User_FavoriteImage",
            foreignKeyDefinition ="FOREIGN KEY(username) REFERENCES user(username) ON DELETE CASCADE ON UPDATE CASCADE" ))
    private User user;
}
