package com.creator.imageAndMusic.domain.service;

import com.creator.imageAndMusic.domain.dto.Criteria;
import com.creator.imageAndMusic.domain.dto.PageDto;
import com.creator.imageAndMusic.domain.entity.*;
import com.creator.imageAndMusic.domain.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class MusicRankingServiceImpl  {



    @Autowired
    private ImageRankingRepository imageRankingRepostiroy;
    @Autowired
    private ImagesFileInfoRepository imagesFileInfoRepository;

    @Autowired
    private MusicRankingRepository musicRankingRepository;

    @Autowired
    private MusicFileInfoRepository musicFileInfoRepository;

    @Autowired
    private MusicRepository musicRepository;


    @Transactional(rollbackFor = Exception.class)
    public boolean addRankingImage(Long fileid) throws Exception {

        if(fileid==null)
            return false;
        MusicFileInfo musicFileInfo =  musicFileInfoRepository.findById(fileid).get();
        log.info("MusicRankingServiceImpl musicFileInfo : ",musicFileInfo);

        //ImagesFileInfo imagesFileInfo2 = imageRankingRepostiroy.findByImagesFileInfo(imagesFileInfo);
        //log.info("ImageRankingServiceImpl isNotNull : ",isNotnull);
        //.info("ImageRankingServiceImpl's is not null :"+(imagesFileInfo.getFileid()==imagesFileInfo2.getFileid()));

        MusicRanking isExistedRnaking = musicRankingRepository.findByMusicFileInfo(musicFileInfo);
        if(isExistedRnaking!=null)
            return false;

        MusicRanking musicRanking = new MusicRanking();
        musicRanking.setRankingId(0L);
        musicRanking.setMusicFileInfo(musicFileInfo);
        musicRanking.setCount(0L);
        musicRanking.setIlikeit(0L);
        musicRanking.setRegDate(LocalDateTime.now());

        musicRankingRepository.save(musicRanking);

        return true;
    }


    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> getAllMusicRanking(Criteria criteria) {

        Map<String,Object> returns = new HashMap<String,Object>();

        //count
        int totalcount =(int)musicRankingRepository.count();

        //PageDto 만들기
        PageDto pagedto = new PageDto(totalcount,criteria);
        //시작 게시물 번호 구하기(수정) - OFFSET
        int offset =(criteria.getPageno()-1) * criteria.getAmount();    //1page = 0, 2page = 10

        //조회순
        List<MusicRanking> list  =  musicRankingRepository.findMusicRankingAmountStart(pagedto.getCriteria().getAmount(),offset);

        PageDto likepagedto = new PageDto(totalcount,criteria);
        int likeoffset =(criteria.getPageno()-1) * criteria.getAmount();

        //좋아요순
        List<MusicRanking> likelist  =  musicRankingRepository.findMusicRankingAmountStartOderByLike(likepagedto.getCriteria().getAmount(),likeoffset);

        //count순 전체 값
        List<MusicRanking> rankingList =  musicRankingRepository.findAllByOrderByCountDesc();

        //
        List<MusicRanking> rankingLikeList =  musicRankingRepository.findAllByOrderByLikeDesc();

        returns.put("list",list);
        returns.put("likelist",likelist);
        returns.put("rankingList",rankingList);
        returns.put("rankingLikeList",rankingLikeList);

        returns.put("pageDto",pagedto);
        returns.put("likepagedto",likepagedto);
        returns.put("count",totalcount);

        return returns;
    }






    @Transactional(rollbackFor = Exception.class)
    public List<MusicRanking> getAllMusicRanking() {

        Map<String,Object> returns = new HashMap<String,Object>();
        return musicRankingRepository.findAll();
    }



    @Transactional(rollbackFor = SQLException.class)
    public void count(Long rankingId) {
        MusicRanking musicRanking =  musicRankingRepository.findById(rankingId).get();
        musicRanking.setCount(musicRanking.getCount()+1);
        musicRankingRepository.save(musicRanking);
    }


    @Transactional(rollbackFor = SQLException.class)
    public MusicRanking getMusicRanking(Long rankingId) {
       return musicRankingRepository.findById(rankingId).get();
    }

    @Transactional(rollbackFor = SQLException.class)
    public List<MusicRanking> getAllMusicRankingByCategory(String subCategory) {
        System.out.println(subCategory);

        subCategory = subCategory.replaceAll("'","");
        List<MusicRanking> list =   musicRankingRepository.findAllByOrderByCountDesc();
        List<MusicRanking> result = new ArrayList<>();
        for(MusicRanking musicRanking : list){
            String sub = musicRanking.getMusicFileInfo().getMusic().getSubCategory();
            if(StringUtils.equals(sub,subCategory)){
                result.add(musicRanking);
            }
        }
        return result;
    }
    @Transactional(rollbackFor = SQLException.class)
    public Map<String, Object> getImageRankingPopular() {
        Map<String,Object> result = new HashMap<>();


        List<MusicRanking> musicTop10ByCount =  musicRankingRepository.findTop10ByOrderByCountDesc();
        List<MusicRanking> musicTop10ByLike =  musicRankingRepository.findTop10ByOrderByIlikeitDesc();
        result.put("musicTop10ByCount",musicTop10ByCount);
        result.put("musicTop10ByLike",musicTop10ByLike);

        return result;
    }
}
