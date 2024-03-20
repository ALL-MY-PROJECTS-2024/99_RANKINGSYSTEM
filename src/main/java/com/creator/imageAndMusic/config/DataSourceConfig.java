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
        dataSource.setUsername("root");
        dataSource.setPassword("1234");

        return dataSource;
    }

}
