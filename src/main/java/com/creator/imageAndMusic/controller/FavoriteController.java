package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.config.auth.jwt.JwtProperties;
import com.creator.imageAndMusic.config.auth.jwt.JwtTokenProvider;
import com.creator.imageAndMusic.domain.service.FavoriteImageServiceImpl;
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
public class FavoriteController {
    @Autowired
    FavoriteImageServiceImpl favoriteImageService;


    @GetMapping("/image/{id}")
    public @ResponseBody ResponseEntity<String> favorites(@PathVariable("id")Long id,Authentication authentication){
        System.out.println("authentication : " + authentication);

        log.info("GET /favorite/image/id : " + id);
        Map<String, Object> result =  favoriteImageService.addFavoriteImage(id);

        return new ResponseEntity( result,HttpStatus.OK);
    }
}
