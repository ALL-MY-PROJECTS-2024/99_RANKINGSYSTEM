package com.creator.imageAndMusic.domain.service;



import com.creator.imageAndMusic.domain.entity.Bookmark;
import com.creator.imageAndMusic.domain.entity.ImagesRanking;
import com.creator.imageAndMusic.domain.entity.User;
import com.creator.imageAndMusic.domain.repository.BookmarkRepository;
import com.creator.imageAndMusic.domain.repository.ImageRankingRepository;
import com.creator.imageAndMusic.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class BookmarkServiceImpl {

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Autowired
    ImageRankingRepository imageRankingRepository;

    @Autowired
    UserRepository userRepository;



    @Transactional(rollbackFor=Exception.class)
    public Map<String,Object> addBookmark(Long ranking_id, String username){
        log.info("addBookmark() ranking_id : " + ranking_id + " username : " + username);
        Optional<ImagesRanking> imagesRankingOptional = imageRankingRepository.findById(ranking_id);
        Map<String,Object> result = new HashMap();
        if(imagesRankingOptional.isEmpty()){
            result.put("imageranking",false);
            return result;
        }



        Optional<User> userOp =  userRepository.findById(username);

        Bookmark bookmark = new Bookmark();
        bookmark.setUser(userOp.get());
        bookmark.setImagesRanking(imagesRankingOptional.get());

        boolean isExisted =  bookmarkRepository.existsByImagesRanking(imagesRankingOptional.get());
        if(isExisted){
            result.put("exist","true");
        }
        else{
            result.put("exist","false");
            bookmarkRepository.save(bookmark);
        }
        return result;
    }


    @Transactional(rollbackFor=Exception.class)
    public List<Bookmark> getBookmark(String username){
        Optional<User> userOptional =  userRepository.findById(username);
        if(userOptional.isEmpty())
            return null;
        return bookmarkRepository.findAllByUserOrderByIdDesc(userOptional.get());
    }
    @Transactional(rollbackFor=Exception.class)
    public void deleteBookmark(Long id) {
        bookmarkRepository.deleteById(id);
    }
}
