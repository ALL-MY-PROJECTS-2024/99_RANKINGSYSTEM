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
import java.util.*;


@Service
@Slf4j
public class MusicRankingServiceImpl {


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

    @Autowired
    private UserRepository userRepository;


    @Transactional(rollbackFor = Exception.class)
    public boolean addRankingImage(Long fileid) throws Exception {

        if (fileid == null)
            return false;
        MusicFileInfo musicFileInfo = musicFileInfoRepository.findById(fileid).get();
        log.info("MusicRankingServiceImpl musicFileInfo : ", musicFileInfo);

        //ImagesFileInfo imagesFileInfo2 = imageRankingRepostiroy.findByImagesFileInfo(imagesFileInfo);
        //log.info("ImageRankingServiceImpl isNotNull : ",isNotnull);
        //.info("ImageRankingServiceImpl's is not null :"+(imagesFileInfo.getFileid()==imagesFileInfo2.getFileid()));

        MusicRanking isExistedRnaking = musicRankingRepository.findByMusicFileInfo(musicFileInfo);
        if (isExistedRnaking != null)
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
    public Map<String, Object> getAllMusicRanking(Criteria criteria) {

        Map<String, Object> returns = new HashMap<String, Object>();

        //count
        int totalcount = (int) musicRankingRepository.count();

        //PageDto 만들기
        PageDto pagedto = new PageDto(totalcount, criteria);
        //시작 게시물 번호 구하기(수정) - OFFSET
        int offset = (criteria.getPageno() - 1) * criteria.getAmount();    //1page = 0, 2page = 10

        //조회순
        List<MusicRanking> list = musicRankingRepository.findMusicRankingAmountStart(pagedto.getCriteria().getAmount(), offset);

        PageDto likepagedto = new PageDto(totalcount, criteria);
        int likeoffset = (criteria.getPageno() - 1) * criteria.getAmount();

        //좋아요순
        List<MusicRanking> likelist = musicRankingRepository.findMusicRankingAmountStartOderByLike(likepagedto.getCriteria().getAmount(), likeoffset);

        //count순 전체 값
        List<MusicRanking> rankingList = musicRankingRepository.findAllByOrderByCountDesc();

        //
        List<MusicRanking> rankingLikeList = musicRankingRepository.findAllByOrderByLikeDesc();

        returns.put("list", list);
        returns.put("likelist", likelist);
        returns.put("rankingList", rankingList);
        returns.put("rankingLikeList", rankingLikeList);

        returns.put("pageDto", pagedto);
        returns.put("likepagedto", likepagedto);
        returns.put("count", totalcount);

        return returns;
    }


    @Transactional(rollbackFor = Exception.class)
    public List<MusicRanking> getAllMusicRanking() {

        Map<String, Object> returns = new HashMap<String, Object>();
        return musicRankingRepository.findAll();
    }


    @Transactional(rollbackFor = SQLException.class)
    public void count(Long rankingId) {
        MusicRanking musicRanking = musicRankingRepository.findById(rankingId).get();
        musicRanking.setCount(musicRanking.getCount() + 1);
        musicRankingRepository.save(musicRanking);
    }


    @Transactional(rollbackFor = SQLException.class)
    public MusicRanking getMusicRanking(Long rankingId) {
        return musicRankingRepository.findById(rankingId).get();
    }

    @Transactional(rollbackFor = SQLException.class)
    public List<MusicRanking> getAllMusicRankingByCategory(String subCategory) {
        System.out.println(subCategory);

        subCategory = subCategory.replaceAll("'", "");
        List<MusicRanking> list = musicRankingRepository.findAllByOrderByCountDesc();
        List<MusicRanking> result = new ArrayList<>();
        for (MusicRanking musicRanking : list) {
            String sub = musicRanking.getMusicFileInfo().getMusic().getSubCategory();
            if (StringUtils.equals(sub, subCategory)) {
                result.add(musicRanking);
            }
        }
        return result;
    }

    @Transactional(rollbackFor = SQLException.class)
    public Map<String, Object> getMusicRankingPopular() {
        Map<String, Object> result = new HashMap<>();


        List<MusicRanking> musicTop10ByCount = musicRankingRepository.findTop10ByOrderByCountDesc();
        List<MusicRanking> musicTop10ByLike = musicRankingRepository.findTop10ByOrderByIlikeitDesc();
        result.put("musicTop10ByCount", musicTop10ByCount);
        result.put("musicTop10ByLike", musicTop10ByLike);

        return result;
    }


    @Transactional(rollbackFor = SQLException.class)
    public Map<String, Object> getAllImageRankingByAllCategory() {
        Map<String, Object> result = new LinkedHashMap<>();

        List<MusicRanking> list =   musicRankingRepository.findAllByOrderByCountDesc();
        System.out.println("개수 : " + list.size());
        List<MusicRanking> Jazz = new ArrayList<>();
        List<MusicRanking> Rock = new ArrayList<>();
        List<MusicRanking> Classic = new ArrayList<>();
        List<MusicRanking> Progressive = new ArrayList<>();
        List<MusicRanking> Advertisement = new ArrayList<>();
        List<MusicRanking> HeavyMetal = new ArrayList<>();
        List<MusicRanking> Pop = new ArrayList<>();
        List<MusicRanking> Others = new ArrayList<>();

        for(MusicRanking musicRanking : list){
            String sub = musicRanking.getMusicFileInfo().getMusic().getSubCategory();
            System.out.println("카테고리 : " + sub);
            if(StringUtils.equals(sub,"Jazz")) {
                Jazz.add(musicRanking);
            }if(StringUtils.equals(sub,"Rock")) {
                Rock.add(musicRanking);
            }if(StringUtils.equals(sub,"Classic")) {
                Classic.add(musicRanking);
            }if(StringUtils.equals(sub,"Progressive")) {
                Progressive.add(musicRanking);
            }if(StringUtils.equals(sub,"Advertisement")) {
                Advertisement.add(musicRanking);
            }if(StringUtils.equals(sub,"HeavyMetal")) {
                HeavyMetal.add(musicRanking);
            }if(StringUtils.equals(sub,"Pop")) {
                Pop.add(musicRanking);
            }if(StringUtils.equals(sub,"Others")) {
                Others.add(musicRanking);
            }
        }
        result.put("Jazz",Jazz);
        result.put("Rock",Rock);
        result.put("Classic",Classic);
        result.put("Progressive",Progressive);
        result.put("Advertisement",Advertisement);
        result.put("HeavyMetal",HeavyMetal);
        result.put("Pop",Pop);
        result.put("Others",Others);

        return result;
    }


    @Transactional(rollbackFor = SQLException.class)
    public Map<String, Object> getLocalMusicRanking() {

        Map<String,Object> datas = new HashMap();

        List<MusicRanking> a = new ArrayList(); //인천
        List<MusicRanking> b = new ArrayList(); //서울
        List<MusicRanking> c = new ArrayList(); //경기
        List<MusicRanking> d = new ArrayList(); //강원
        List<MusicRanking> e = new ArrayList(); //충남
        List<MusicRanking> f = new ArrayList(); //세종
        List<MusicRanking> g = new ArrayList(); //대전
        List<MusicRanking> h = new ArrayList(); //충북
        List<MusicRanking> i = new ArrayList(); //경북
        List<MusicRanking> j = new ArrayList(); //대구
        List<MusicRanking> k = new ArrayList(); //전남
        List<MusicRanking> l = new ArrayList(); //광주
        List<MusicRanking> m = new ArrayList(); //경남
        List<MusicRanking> n = new ArrayList(); //부산
        List<MusicRanking> o = new ArrayList(); //울산
        List<MusicRanking> p = new ArrayList(); //제주


        List<MusicRanking> list  = musicRankingRepository.findAll();

        list.forEach(musicRanking->{
            String username =  musicRanking.getMusicFileInfo().getMusic().getUsername();
            User user =  userRepository.findById(username).get();
            System.out.println("user : " + user);
            if(user.getAddr1()!=null && user.getAddr1().contains("인천")){
                a.add(musicRanking);
            }else if(user.getAddr1()!=null && user.getAddr1().contains("서울")){
                b.add(musicRanking);
            }else if(user.getAddr1()!=null &&user.getAddr1().contains("경기")){
                c.add(musicRanking);
            }else if(user.getAddr1()!=null &&user.getAddr1().contains("강원")){
                d.add(musicRanking);
            }else if(user.getAddr1()!=null &&user.getAddr1().contains("충남")){
                e.add(musicRanking);
            }else if(user.getAddr1()!=null &&user.getAddr1().contains("세종")){
                f.add(musicRanking);
            }else if(user.getAddr1()!=null &&user.getAddr1().contains("대전")){
                g.add(musicRanking);
            }else if(user.getAddr1()!=null &&user.getAddr1().contains("충북")){
                h.add(musicRanking);
            }else if(user.getAddr1()!=null && user.getAddr1().contains("경북")){
                i.add(musicRanking);
            }else if(user.getAddr1()!=null &&user.getAddr1().contains("대구")){
                j.add(musicRanking);
            }else if(user.getAddr1()!=null &&user.getAddr1().contains("전남")){
                k.add(musicRanking);
            }else if(user.getAddr1()!=null &&user.getAddr1().contains("광주")){
                l.add(musicRanking);
            }else if(user.getAddr1()!=null &&user.getAddr1().contains("경남")){
                m.add(musicRanking);
            }else if(user.getAddr1()!=null &&user.getAddr1().contains("부산")){
                n.add(musicRanking);
            }else if(user.getAddr1()!=null &&user.getAddr1().contains("울산")){
                o.add(musicRanking);
            }else if(user.getAddr1()!=null &&user.getAddr1().contains("제주")){
                p.add(musicRanking);
            }
        });

        //정렬 하기(지금 말고 나중에)

        //각 지역으로 저장
        datas.put("인천광역시",a);
        datas.put("서울특별시",b);
        datas.put("경기도",c);
        datas.put("강원도",d);
        datas.put("충청남도",e);
        datas.put("세종특별자치시",f);
        datas.put("대전광역시",g);
        datas.put("충청북도",h);
        datas.put("경상북도",i);
        datas.put("대구광역시",j);
        datas.put("전라남도",k);
        datas.put("광주광역시",l);
        datas.put("경상남도",m);
        datas.put("부산광역시",n);
        datas.put("울산광역시",o);
        datas.put("제주특별자치도",p);


        return datas;
    }
    @Transactional(rollbackFor = SQLException.class)
    public Map<String, Object> getAllMusicRankingByAllCategoryCount() {
        Map<String,Object> result = new LinkedHashMap<>();
        List<MusicRanking> list =   musicRankingRepository.findAllByOrderByCountDesc();
        Integer Jazz =0;
        Integer Rock =0;
        Integer Classic =0;
        Integer Progressive =0;
        Integer Advertisement =0;
        Integer HeavyMetal =0;
        Integer Pop =0;
        Integer Others =0;
        for(MusicRanking musicRanking : list){
            String sub = musicRanking.getMusicFileInfo().getMusic().getSubCategory();
            if(StringUtils.equals(sub,"Jazz")){
                Jazz++;
            }if(StringUtils.equals(sub,"Rock")){
                Rock++;
            }if(StringUtils.equals(sub,"Classic")){
                Classic++;
            }if(StringUtils.equals(sub,"Progressive")){
                Progressive++;
            }if(StringUtils.equals(sub,"Advertisement")){
                Advertisement++;
            }if(StringUtils.equals(sub,"HeavyMetal")){
                HeavyMetal++;
            }if(StringUtils.equals(sub,"Pop")){
                Pop++;
            }if(StringUtils.equals(sub,"Others")){
                Others++;
            }
        }
        result.put("Jazz",Jazz);
        result.put("Rock",Rock);
        result.put("Classic",Classic);
        result.put("Progressive",Progressive);
        result.put("Advertisement",Advertisement);
        result.put("HeavyMetal",HeavyMetal);
        result.put("Pop",Pop);
        result.put("Others",Others);
        return result;

    }
}