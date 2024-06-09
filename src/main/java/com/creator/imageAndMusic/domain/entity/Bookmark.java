package com.creator.imageAndMusic.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "username",foreignKey = @ForeignKey(name="FK_User_Bookmark",
            foreignKeyDefinition ="FOREIGN KEY(username) REFERENCES user(username) ON DELETE CASCADE ON UPDATE CASCADE" ))
    User user;

    @ManyToOne
    @JoinColumn(name = "ranking_id",foreignKey = @ForeignKey(name="FK_ImagesRanking_Bookmark",
            foreignKeyDefinition ="FOREIGN KEY(ranking_id) REFERENCES images_ranking(ranking_id) ON DELETE CASCADE ON UPDATE CASCADE" ))
    ImagesRanking imagesRanking;
}
