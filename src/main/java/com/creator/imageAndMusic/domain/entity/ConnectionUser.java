package com.creator.imageAndMusic.domain.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@IdClass(ConnectionUserId.class)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConnectionUser {
    @Id
    private LocalDate today;
    @Id
    private String remoteIp;
    @Id
    private String username;

    Long count ;
    Long repeatPerSecond;
    String requestUri;
}
