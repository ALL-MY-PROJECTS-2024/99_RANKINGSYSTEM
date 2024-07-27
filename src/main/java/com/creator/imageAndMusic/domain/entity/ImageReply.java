package com.creator.imageAndMusic.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ImageReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "iamgeid", foreignKey = @ForeignKey(name="FK_ImageReply_Image_Read_1",
            foreignKeyDefinition ="FOREIGN KEY(iamgeid) REFERENCES images(iamgeid) ON DELETE CASCADE ON UPDATE CASCADE" ))
    private Images image;

    @ManyToOne
    @JoinColumn(name = "username",foreignKey = @ForeignKey(name="FK_ImageReply_User_Read_1",
            foreignKeyDefinition ="FOREIGN KEY(username) REFERENCES user(username) ON DELETE CASCADE ON UPDATE CASCADE" ))
    private User user;

    private LocalDateTime date;

    private String context;


}
