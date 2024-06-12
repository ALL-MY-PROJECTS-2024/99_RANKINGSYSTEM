package com.creator.imageAndMusic.domain.service;


import com.creator.imageAndMusic.config.auth.PrincipalDetails;
import com.creator.imageAndMusic.config.auth.jwt.JwtProperties;
import com.creator.imageAndMusic.config.auth.jwt.JwtTokenProvider;
import com.creator.imageAndMusic.domain.entity.*;
import com.creator.imageAndMusic.domain.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class FavoriteMusicServiceImpl {

    @Autowired
    private FavoriteMusicRepository favoriteMusicRepository;
    @Autowired
    private MusicRepository musicRepository;
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private MusicRankingRepository musicRankingRepository;

    @Autowired
    HttpServletRequest request;

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Transactional(rollbackFor=Exception.class)
    public  Map<String,Object> addFavoriteMusic(Long rankingId){
        Optional<MusicRanking> imgOp= musicRankingRepository.findById(rankingId);
        Map<String,Object> result = new HashMap();
        if(imgOp.isEmpty()){
            result.put("musicranking",false);
            return result;
        }
        MusicRanking musicRanking = imgOp.get();


        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(JwtProperties.COOKIE_NAME)).findFirst()
                .map(cookie -> cookie.getValue())
                .orElse(null);
        Authentication authentication =  jwtTokenProvider.getAuthentication(token);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetails.getUsername();

        Optional<User> userOp = userRepository.findById(username);

        if(userOp.isEmpty()){
            result.put("user",false);
            return result;
        }

        User user = userOp.get();

        boolean isExistedUser =favoriteMusicRepository.existsByUserAndMusicRanking(user,musicRanking);
        if(isExistedUser){
            favoriteMusicRepository.deleteByUserAndMusicRanking(user,musicRanking);

            result.put("favorite",true);
        }else{
            FavoriteMusic favoriteMusic = new FavoriteMusic();
            favoriteMusic.setUser(userOp.get());
            favoriteMusic.setMusicRanking(musicRanking);
            favoriteMusicRepository.save(favoriteMusic);

            result.put("favorite",false);
        }

        Long count= favoriteMusicRepository.countByMusicRanking(musicRanking);
        musicRanking.setIlikeit(count);
        musicRankingRepository.save(musicRanking);

        result.put("count",count);


        return result;
    }

    @Transactional(rollbackFor=Exception.class)
    public List<FavoriteMusic> getMyfavoriteMusic(String name) {
        Optional<User> userOp = userRepository.findById(name);
        if(userOp.isEmpty()){
            return null;
        }
        return favoriteMusicRepository.findByUser(userOp.get());

    }
}
