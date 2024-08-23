package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.config.auth.jwt.JwtProperties;
import com.creator.imageAndMusic.config.auth.jwt.JwtTokenProvider;
import com.creator.imageAndMusic.domain.service.FavoriteImageServiceImpl;
import com.creator.imageAndMusic.domain.service.FavoriteMusicServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/favorite")
@Tag(name = "좋아요 기능", description = "이미지 좋아요 / 음악 좋아요 버튼")
public class FavoriteController {
    @Autowired
    FavoriteImageServiceImpl favoriteImageService;

    @Autowired
    FavoriteMusicServiceImpl favoriteMusicService;


    @Operation(
            summary = "홈>이달의콘텐츠RANKING>전체 이미지 랭킹",
            description = "이미지 좋아요 추가",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/image/{id}")
    public @ResponseBody ResponseEntity<String> favorites(@PathVariable("id")Long id,Authentication authentication){
        System.out.println("authentication : " + authentication);

        log.info("GET /favorite/image/id : " + id);
        Map<String, Object> result =  favoriteImageService.addFavoriteImage(id);

        return new ResponseEntity( result,HttpStatus.OK);
    }

    @Operation(
            summary = "홈>이달의콘텐츠RANKING>전체 음악 랭킹",
            description = "음악 좋아요 추가",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/music/{id}")
    public @ResponseBody ResponseEntity<String> favorites_music(@PathVariable("id")Long id,Authentication authentication){
        System.out.println("authentication : " + authentication);
        log.info("GET /favorite/image/id : " + id);
        Map<String, Object> result =  favoriteMusicService.addFavoriteMusic(id);
        return new ResponseEntity( result,HttpStatus.OK);
    }

}
