package com.creator.imageAndMusic.domain.dto;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ConnectionUserDto {

    private LocalDate today;
    private String remoteIp;
    private String username;

    Long count ;
    Long repeatPerSecond;
    String requestUri;

}
