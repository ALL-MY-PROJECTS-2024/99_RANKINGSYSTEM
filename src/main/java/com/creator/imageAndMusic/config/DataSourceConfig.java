package com.creator.imageAndMusic.config;


import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

    @Bean
    public HikariDataSource dataSource(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/CreatorDB");
        dataSource.setUsername("dbconn");
        dataSource.setPassword("Zhfldk11!");

        return dataSource;
    }

}
