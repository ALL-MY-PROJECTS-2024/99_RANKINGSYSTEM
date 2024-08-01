package com.creator.imageAndMusic.domain.repository;


import com.creator.imageAndMusic.domain.entity.ConnectionUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface ConnectionUserRepository extends JpaRepository<ConnectionUser, LocalDate> {
    ConnectionUser findByTodayAndRemoteIpAndUsername(LocalDate today, String remoteIp, String username);


}
