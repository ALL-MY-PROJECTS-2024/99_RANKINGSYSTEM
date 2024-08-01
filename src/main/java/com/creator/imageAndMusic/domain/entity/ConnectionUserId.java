package com.creator.imageAndMusic.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionUserId implements Serializable {
    private LocalDate today;
    private String remoteIp;
    private String username;
}