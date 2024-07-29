package com.creator.imageAndMusic.domain.service;

import com.creator.imageAndMusic.config.auth.PrincipalDetails;
import com.creator.imageAndMusic.domain.dto.Criteria;
import com.creator.imageAndMusic.domain.dto.PageDto;
import com.creator.imageAndMusic.domain.dto.UserDto;
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
import java.util.List;


@Service
@Slf4j
public class ImageRankingServiceImpl {


    @Autowired
    ApplicationContext appContext;

    @Autowired
    private ImageRankingRepository imageRankingRepostiroy;
    @Autowired
    private ImagesFileInfoRepository imagesFileInfoRepository;

    @Autowired
    private ImagesRepository imagesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageReplyRepository imageReplyRepository;

    @Transactional(rollbackFor = Exception.class)
    public boolean addRankingImage(Long fileid) throws Exception {

        if(fileid==null)
            return false;
        ImagesFileInfo imagesFileInfo =  imagesFileInfoRepository.findById(fileid).get();
        log.info("ImageRankingServiceImpl imagesFileInfo : ",imagesFileInfo);

        //ImagesFileInfo imagesFileInfo2 = imageRankingRepostiroy.findByImagesFileInfo(imagesFileInfo);
        //log.info("ImageRankingServiceImpl isNotNull : ",isNotnull);
        //.info("ImageRankingServiceImpl's is not null :"+(imagesFileInfo.getFileid()==imagesFileInfo2.getFileid()));

        ImagesRanking isExistedRnaking = imageRankingRepostiroy.findByImagesFileInfo(imagesFileInfo);
        if(isExistedRnaking!=null)
            return false;

        ImagesRanking imagesRanking = new ImagesRanking();
        imagesRanking.setRankingId(0L);
        imagesRanking.setImagesFileInfo(imagesFileInfo);
        imagesRanking.setCount(0L);
        imagesRanking.setIlikeit(0L);
        imagesRanking.setRegDate(LocalDateTime.now());

        imageRankingRepostiroy.save(imagesRanking);

        return true;
    }


    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> getAllImageRanking(Criteria criteria) {

        Map<String,Object> returns = new HashMap<String,Object>();

        //count
        int totalcount =(int)imageRankingRepostiroy.count();

        //PageDto 만들기
        PageDto pagedto = new PageDto(totalcount,criteria);
        //시작 게시물 번호 구하기(수정) - OFFSET
        int offset =(criteria.getPageno()-1) * criteria.getAmount();    //1page = 0, 2page = 10

        //조회순
        List<ImagesRanking> list  =  imageRankingRepostiroy.findImagesRankingAmountStart(pagedto.getCriteria().getAmount(),offset);

        PageDto likepagedto = new PageDto(totalcount,criteria);
        int likeoffset =(criteria.getPageno()-1) * criteria.getAmount();
        //좋아요순
        List<ImagesRanking> likelist  =  imageRankingRepostiroy.findImagesRankingAmountStartOderByLike(likepagedto.getCriteria().getAmount(),likeoffset);

        //count순 전체 값
        List<ImagesRanking> rankingList =  imageRankingRepostiroy.findAllByOrderByCountDesc();

        //
        List<ImagesRanking> rankingLikeList =  imageRankingRepostiroy.findAllByOrderByLikeDesc();

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
    public List<ImagesRanking> getAllImageRanking() {

        Map<String,Object> returns = new HashMap<String,Object>();
        return imageRankingRepostiroy.findAll();
    }


    @Transactional(rollbackFor = SQLException.class)
    public void count(Long rankingId) {
        ImagesRanking imageRanking =  imageRankingRepostiroy.findById(rankingId).get();
        imageRanking.setCount(imageRanking.getCount()+1);
        imageRankingRepostiroy.save(imageRanking);
    }

    @Transactional(rollbackFor = SQLException.class)
    public ImagesRanking getImageRanking(Long rankingId) {

        return imageRankingRepostiroy.findById(rankingId).get();
    }



    //지역별로 묶인 이미지 정보가져오기(USER가 Ranking등록한 이미지 가져와서 지역별로 선별하기)
    @Transactional(rollbackFor = SQLException.class)
    public Map<String,Object> getLocalImageRanking(){

        Map<String,Object> datas = new HashMap();

        List<ImagesRanking> a = new ArrayList(); //인천
        List<ImagesRanking> b = new ArrayList(); //서울
        List<ImagesRanking> c = new ArrayList(); //경기
        List<ImagesRanking> d = new ArrayList(); //강원
        List<ImagesRanking> e = new ArrayList(); //충남
        List<ImagesRanking> f = new ArrayList(); //세종
        List<ImagesRanking> g = new ArrayList(); //대전
        List<ImagesRanking> h = new ArrayList(); //충북
        List<ImagesRanking> i = new ArrayList(); //경북
        List<ImagesRanking> j = new ArrayList(); //대구
        List<ImagesRanking> k = new ArrayList(); //전남
        List<ImagesRanking> l = new ArrayList(); //광주
        List<ImagesRanking> m = new ArrayList(); //경남
        List<ImagesRanking> n = new ArrayList(); //부산
        List<ImagesRanking> o = new ArrayList(); //울산
        List<ImagesRanking> p = new ArrayList(); //제주


        List<ImagesRanking> list  = imageRankingRepostiroy.findAll();

        list.forEach(imageRanking->{
             String username =  imageRanking.getImagesFileInfo().getImages().getUsername();
             User user =  userRepository.findById(username).get();
             System.out.println("user : " + user);
             if(user.getAddr1()!=null && user.getAddr1().contains("인천")){
                 a.add(imageRanking);
             }else if(user.getAddr1()!=null && user.getAddr1().contains("서울")){
                 b.add(imageRanking);
             }else if(user.getAddr1()!=null &&user.getAddr1().contains("경기")){
                 c.add(imageRanking);
             }else if(user.getAddr1()!=null &&user.getAddr1().contains("강원")){
                 d.add(imageRanking);
             }else if(user.getAddr1()!=null &&user.getAddr1().contains("충남")){
                 e.add(imageRanking);
             }else if(user.getAddr1()!=null &&user.getAddr1().contains("세종")){
                 f.add(imageRanking);
             }else if(user.getAddr1()!=null &&user.getAddr1().contains("대전")){
                 g.add(imageRanking);
             }else if(user.getAddr1()!=null &&user.getAddr1().contains("충북")){
                 h.add(imageRanking);
             }else if(user.getAddr1()!=null && user.getAddr1().contains("경북")){
                 i.add(imageRanking);
             }else if(user.getAddr1()!=null &&user.getAddr1().contains("대구")){
                 j.add(imageRanking);
             }else if(user.getAddr1()!=null &&user.getAddr1().contains("전남")){
                 k.add(imageRanking);
             }else if(user.getAddr1()!=null &&user.getAddr1().contains("광주")){
                 l.add(imageRanking);
             }else if(user.getAddr1()!=null &&user.getAddr1().contains("경남")){
                 m.add(imageRanking);
             }else if(user.getAddr1()!=null &&user.getAddr1().contains("부산")){
                 n.add(imageRanking);
             }else if(user.getAddr1()!=null &&user.getAddr1().contains("울산")){
                 o.add(imageRanking);
             }else if(user.getAddr1()!=null &&user.getAddr1().contains("제주")){
                 p.add(imageRanking);
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
    public List<ImagesRanking> getAllImageRankingByCategory(String subCategory) {
        System.out.println(subCategory);

        subCategory = subCategory.replaceAll("'","");
        List<ImagesRanking> list =   imageRankingRepostiroy.findAllByOrderByCountDesc();
        List<ImagesRanking> result = new ArrayList<>();
        for(ImagesRanking imagesRanking : list){
            String sub = imagesRanking.getImagesFileInfo().getImages().getSubCategory();
            if(StringUtils.equals(sub,subCategory)){
                result.add(imagesRanking);
            }
        }
        return result;
    }


    @Transactional(rollbackFor = SQLException.class)
    public Map<String,Object> getAllImageRankingByCategory(String subCategory, Criteria criteria) {
        subCategory = subCategory.replaceAll("'","");
        System.out.println(subCategory);
        Map<String,Object> result = new HashMap<>();
        int totalCount=(int)imageRankingRepostiroy.findImageRankingSubCategoryCount(subCategory);
        System.out.println("totalCount  :" + totalCount);
        //pageDto
        PageDto pagedto = new PageDto(totalCount,1,criteria);
        //offset
        int offset =(criteria.getPageno()-1) * criteria.getAmount();    //1page = 0, 2page = 10


        subCategory = subCategory.replaceAll("'","");


        //List<ImagesRanking> list =   imageRankingRepostiroy.findAllByOrderByCountDesc();
        List<ImagesRanking> list =   imageRankingRepostiroy.findImageRankingAmountStart(subCategory,criteria.getAmount(),offset);

        result.put("list",list);
        result.put("pageDto",pagedto);
        result.put("totalCount",totalCount);
        return result;

    }

    @Transactional(rollbackFor = SQLException.class)
    public Map<String, Object> getImageRankingPopular() {
        Map<String,Object> result = new LinkedHashMap<>();

        List<ImagesRanking> imageTop10ByCount =  imageRankingRepostiroy.findTop10ByOrderByCountDesc();
        List<ImagesRanking> imageTop10ByLike =  imageRankingRepostiroy.findTop10ByOrderByIlikeitDesc();
        result.put("imageTop10ByCount",imageTop10ByCount);
        result.put("imageTop10ByLike",imageTop10ByLike);
        return result;
    }


    @Transactional(rollbackFor = SQLException.class)
    public Map<String, Object> getAllImageRankingByAllCategory() {

        Map<String,Object> result = new LinkedHashMap<>();
        List<ImagesRanking> list =   imageRankingRepostiroy.findAllByOrderByCountDesc();
        List<ImagesRanking> Character = new ArrayList<>();
        List<ImagesRanking> City = new ArrayList<>();
        List<ImagesRanking> Map = new ArrayList<>();
        List<ImagesRanking> iCON = new ArrayList<>();
        List<ImagesRanking> Furniture = new ArrayList<>();
        List<ImagesRanking> Car = new ArrayList<>();
        List<ImagesRanking> Place = new ArrayList<>();
        List<ImagesRanking> Others = new ArrayList<>();
        for(ImagesRanking imagesRanking : list){
            String sub = imagesRanking.getImagesFileInfo().getImages().getSubCategory();
            if(StringUtils.equals(sub,"Character")){
                Character.add(imagesRanking);
            }if(StringUtils.equals(sub,"City")){
                City.add(imagesRanking);
            }if(StringUtils.equals(sub,"Map")){
                Map.add(imagesRanking);
            }if(StringUtils.equals(sub,"iCON")){
                iCON.add(imagesRanking);
            }if(StringUtils.equals(sub,"Furniture")){
                Furniture.add(imagesRanking);
            }if(StringUtils.equals(sub,"Car")){
                Car.add(imagesRanking);
            }if(StringUtils.equals(sub,"Place")){
                Place.add(imagesRanking);
            }if(StringUtils.equals(sub,"Others")){
                Others.add(imagesRanking);
            }
        }
        result.put("Character",Character);
        result.put("City",City);
        result.put("Map",Map);
        result.put("iCON",iCON);
        result.put("Furniture",Furniture);
        result.put("Car",Car);
        result.put("Place",Place);
        result.put("Others",Others);
        return result;
    }



    public ImageReply addReply(String context, Long imageId,String username) {
        Optional<User> userOptional =  userRepository.findById(username);
        Optional<Images> imageOptional =  imagesRepository.findById(imageId);
        if(userOptional.isEmpty()) {
            return null;
        }
        if(imageOptional.isEmpty()){
            return null;
        }
        User user = userOptional.get();
        Images image = imageOptional.get();

        ImageReply reply = new ImageReply();
        reply.setContext(context);
        reply.setUser(user);
        reply.setImage(image);
        reply.setDate(LocalDateTime.now());

        imageReplyRepository.save(reply);

        return reply;
    }


    public List<ImageReply> getAllReply(Long iamgeId) {
        Optional<Images> imageOptional =  imagesRepository.findById(iamgeId);
        if(imageOptional.isEmpty()){
            return null;
        }

        List<ImageReply> list =  imageReplyRepository.findAllByImageOrderByIdDesc(imageOptional.get());

        return list;
    }


    public Map<String, Object> deleteReply(Long id, PrincipalDetails principalDetails) {
        Map<String, Object> result = new HashMap<String, Object>();;
        Optional<ImageReply> imageReplyOptional =  imageReplyRepository.findById(id);
        if(imageReplyOptional.isEmpty()){
            result.put("message","댓글이 존재하지 않습니다.");
            result.put("success",false);
            return result;
        }
        ImageReply imageReply = imageReplyOptional.get();
        UserDto userDto = principalDetails.getUserDto();
        if(StringUtils.equals("ROLE_ADMIN",userDto.getRole())){
            result.put("message","관리자에 의해 댓글이 삭제되었습니다.");
            result.put("success",true);
            imageReplyRepository.deleteById(id);
            return result;
        }

        if(!StringUtils.equals(userDto.getUsername(),imageReply.getUser().getUsername())){
            result.put("message","삭제를 할수 있는 권한이 없습니다.");
            result.put("success",false);
            return result;
        }
        result.put("message","댓글삭제 완료");
        result.put("success",true);
        imageReplyRepository.deleteById(id);
        return result;
    }


    @Transactional(rollbackFor = SQLException.class)
    public Map<String, Object> getAllImageRankingByAllCategoryCount()
    {
        Map<String,Object> result = new LinkedHashMap<>();
        List<ImagesRanking> list =   imageRankingRepostiroy.findAllByOrderByCountDesc();
        Integer Character =0;
        Integer City =0;
        Integer Map =0;
        Integer iCON =0;
        Integer Furniture =0;
        Integer Car =0;
        Integer Place =0;
        Integer Others =0;
        for(ImagesRanking imagesRanking : list){
            String sub = imagesRanking.getImagesFileInfo().getImages().getSubCategory();
            if(StringUtils.equals(sub,"Character")){
                Character++;
            }if(StringUtils.equals(sub,"City")){
                City++;
            }if(StringUtils.equals(sub,"Map")){
                Map++;
            }if(StringUtils.equals(sub,"iCON")){
                iCON++;
            }if(StringUtils.equals(sub,"Furniture")){
                Furniture++;
            }if(StringUtils.equals(sub,"Car")){
                Car++;
            }if(StringUtils.equals(sub,"Place")){
                Place++;
            }if(StringUtils.equals(sub,"Others")){
                Others++;
            }
        }
        result.put("Character",Character);
        result.put("City",City);
        result.put("Map",Map);
        result.put("iCON",iCON);
        result.put("Furniture",Furniture);
        result.put("Car",Car);
        result.put("Place",Place);
        result.put("Others",Others);
        return result;

    }

}
