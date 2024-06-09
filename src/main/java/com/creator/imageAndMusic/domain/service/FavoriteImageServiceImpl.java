package com.creator.imageAndMusic.domain.service;


import com.creator.imageAndMusic.config.auth.PrincipalDetails;
import com.creator.imageAndMusic.config.auth.jwt.JwtProperties;
import com.creator.imageAndMusic.config.auth.jwt.JwtTokenProvider;
import com.creator.imageAndMusic.domain.entity.FavoriteImage;
import com.creator.imageAndMusic.domain.entity.Images;
import com.creator.imageAndMusic.domain.entity.ImagesRanking;
import com.creator.imageAndMusic.domain.entity.User;
import com.creator.imageAndMusic.domain.repository.FavoriteImageRepository;
import com.creator.imageAndMusic.domain.repository.ImageRankingRepository;
import com.creator.imageAndMusic.domain.repository.ImagesRepository;
import com.creator.imageAndMusic.domain.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class FavoriteImageServiceImpl {

    @Autowired
    private FavoriteImageRepository favoriteImageRepository;
    @Autowired
    private ImagesRepository imagesRepository;
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ImageRankingRepository imageRankingRepository;

    @Autowired
    HttpServletRequest request;

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Transactional(rollbackFor=Exception.class)
    public  Map<String,Object> addFavoriteImage(Long rankingId){
        Optional<ImagesRanking> imgOp= imageRankingRepository.findById(rankingId);
        Map<String,Object> result = new HashMap();
        if(imgOp.isEmpty()){
            result.put("imageranking",false);
            return result;
        }
        ImagesRanking imagesRanking = imgOp.get();


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

        boolean isExistedUser =favoriteImageRepository.existsByUserAndImagesRanking(user,imagesRanking);
        if(isExistedUser){
            favoriteImageRepository.deleteByUserAndImagesRanking(user,imagesRanking);

            result.put("favorite",true);


        }else{
            FavoriteImage favoriteImage = new FavoriteImage();
            favoriteImage.setUser(userOp.get());
            favoriteImage.setImagesRanking(imagesRanking);
            favoriteImageRepository.save(favoriteImage);


            result.put("favorite",false);
        }

        Long count= favoriteImageRepository.countByImagesRanking(imagesRanking);
        imagesRanking.setIlikeit(count);
        imageRankingRepository.save(imagesRanking);

        result.put("count",count);


        return result;
    }


    @Transactional(rollbackFor=Exception.class)
    public List<FavoriteImage> getMyfavoriteImage(String name) {
        Optional<User> userOp = userRepository.findById(name);
        if(userOp.isEmpty()){
            return null;
        }
        return favoriteImageRepository.findByUser(userOp.get());

    }
}
