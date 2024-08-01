package com.creator.imageAndMusic.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
    2024-08-01
    프로젝트 내 모든 API 요청/응답 DOCUMENTATION
    작성자 : 정우균
    https://m.post.naver.com/viewer/postView.naver?volumeNo=35110996&memberNo=5733062
*/
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("CHAIF 이미지/음악 경매 싸이트")
                .version("1.0")
                .description("요청/응답에 관련된 BACKEND API DOCUMENT 입니다.");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
