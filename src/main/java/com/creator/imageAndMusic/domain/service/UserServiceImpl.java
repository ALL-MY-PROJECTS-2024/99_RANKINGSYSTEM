package com.creator.imageAndMusic.domain.service;


import com.creator.imageAndMusic.config.auth.PrincipalDetails;
import com.creator.imageAndMusic.config.auth.jwt.JwtProperties;
import com.creator.imageAndMusic.config.auth.jwt.JwtTokenProvider;
import com.creator.imageAndMusic.domain.dto.AlbumDto;
import com.creator.imageAndMusic.domain.dto.UserDto;
import com.creator.imageAndMusic.domain.entity.*;
import com.creator.imageAndMusic.domain.repository.*;
import com.creator.imageAndMusic.properties.AUTH;
import com.creator.imageAndMusic.properties.UPLOADPATH;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ImagesRepository imagesRepository;

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private ImagesFileInfoRepository imagesFileInfoRepository;

    @Autowired
    private MusicFileInfoRepository musicFileInfoRepository;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private HttpServletRequest httpServletRequest;


    @Transactional(rollbackFor = Exception.class)
    public boolean memberJoin(UserDto dto, Model model, HttpServletRequest request) throws Exception{

        //비지니스 Validation Check

        //password vs repassword 일치여부
        if(!dto.getPassword().equals(dto.getRepassword()) ){
            model.addAttribute("message","패스워드 입력이 상이합니다 다시 입력하세요");
            System.out.println("UserServiceImpl's memberJoin .. 패스워드 불일치");
            return false;
        }

        //동일 계정이 있는지 여부 확인
        if(userRepository.existsById(dto.getUsername())){
            model.addAttribute("message","동일한 계정명이 존재합니다.");
            System.out.println("UserServiceImpl's memberJoin .. 동일한 계정명이 존재");
            return false;
        }



        //이메일인증이 되었는지 확인(JWT EmailAuth쿠키 true확인)
        Cookie[] cookies =  request.getCookies();
        String jwtAccessToken = Arrays.stream(cookies).filter(co -> co.getName().equals(AUTH.EMAIL_COOKIE_NAME)).findFirst()
                .map(co -> co.getValue())
                .orElse(null);

        log.info("JWT ACCESS TOKEN : " + jwtAccessToken);

//        //---
//        // JWT토큰의 만료여부 확인
//        //---
        if( !jwtTokenProvider.validateToken(jwtAccessToken)){
            model.addAttribute("message","이메일 인증 유효시간을 초과했습니다");
            System.out.println("UserServiceImpl's memberJoin .. 이메일 인증 유효시간을 초과");
            return false;
        }
        else{
            //EmailAuth Claim Value값 꺼내서 true 확인
            Claims claims = jwtTokenProvider.parseClaims(jwtAccessToken);
            Boolean isEmailAuth = (Boolean)claims.get(AUTH.EMAIL_COOKIE_NAME);
            String id = (String)claims.get("id");
            log.info("이메일 인증 여부 : "+ isEmailAuth);

            if(isEmailAuth==null || isEmailAuth!=true){
                //이메일인증실패!!
                model.addAttribute("message","해당 계정의 이메일 인증이 되어있지 않습니다.");
                System.out.println("UserServiceImpl's memberJoin .. 해당 계정의 이메일 인증이 되어있지 않습니다.");
                return false;
            }

        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        //Dto->Entity
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setNickname(dto.getNickname());
        user.setPhone(dto.getPhone());
        user.setZipcode(dto.getZipcode());
        user.setAddr1(dto.getAddr1());
        user.setAddr2(dto.getAddr2());
        user.setRole("ROLE_USER");


        //Db Saved...
        userRepository.save(user);



        return true;
    }


    @Override
    public boolean uploadAlbum(AlbumDto dto) throws IOException {

        if(dto.getMainCategory().equals("image"))
            return uploadImage(dto);
        else
            return uploadMusic(dto);

    }


    @Transactional(rollbackFor = Exception.class)
    public boolean uploadImage(AlbumDto dto) throws IOException {
        //참고
        //https://github.com/TMP-SPRINGBOOT/SPRINBBOOT_PROJECTS/blob/main/07%20IMAGEBOARD/src/main/java/com/example/demo/domain/service/ImageBoardService.java

        //Dto->ImagesEntity
        Images images = new Images();

        images.setMainCategory(dto.getMainCategory());
        images.setSubCategory(dto.getSubCategory());
        images.setTitle(dto.getTitle());
        images.setDescription(dto.getDescription());
        images.setUsername(dto.getUsername());
        images.setCreateAt(LocalDateTime.now());
        images.setLat(dto.getLat());
        images.setLng(dto.getLng());
        imagesRepository.save(images);

        //저장 폴더 지정()
        String uploadPath= UPLOADPATH.ROOTDIRPATH+ File.separator+UPLOADPATH.UPPERDIRPATH+ File.separator;
        uploadPath+=UPLOADPATH.IMAGEDIRPATH+ File.separator+dto.getUsername()+ File.separator+dto.getSubCategory()+File.separator+images.getIamgeid();

        File dir = new File(uploadPath);
        if(!dir.exists())
            dir.mkdirs();

        for(MultipartFile file : dto.getFiles())
        {
            System.out.println("-----------------------------");
            System.out.println("filename : " + file.getName());
            System.out.println("filename(origin) : " + file.getOriginalFilename());
            System.out.println("filesize : " + file.getSize());
            System.out.println("-----------------------------");
            File fileobj = new File(dir,file.getOriginalFilename());    //파일객체생성

            file.transferTo(fileobj);   //저장


            //--------------------------------
            //OLD
            //--------------------------------
            //섬네일 생성
//            File thumbnailFile = new File(dir,"s_"+file.getOriginalFilename());
//            BufferedImage bo_image =  ImageIO.read(fileobj);
//            BufferedImage bt_image = new BufferedImage(250,250,BufferedImage.TYPE_3BYTE_BGR);
//            Graphics2D graphic =bt_image.createGraphics();
//            graphic.drawImage(bo_image,0,0,250,250,null);
//            ImageIO.write(bt_image,"png",thumbnailFile);
//
//            //DB에 파일경로 저장
//            ImagesFileInfo imageBoardFileInfo = new ImagesFileInfo();
//            imageBoardFileInfo.setImages(images);
//            String dirPath= File.separator+UPLOADPATH.UPPERDIRPATH+ File.separator;
//            dirPath+=UPLOADPATH.IMAGEDIRPATH+ File.separator+dto.getUsername()+ File.separator+dto.getSubCategory()+File.separator+images.getIamgeid();
//            imageBoardFileInfo.setDir(dirPath);
//            imageBoardFileInfo.setFilename(file.getOriginalFilename());
//            imagesFileInfoRepository.save(imageBoardFileInfo);

            //NEW
            // 섬네일 생성
            createThumbnail(fileobj, dir);

            // DB에 파일경로 저장
            saveFileInfo(images, dto, fileobj,file);
        }

        return true;
    }

    private void createThumbnail(File file, File dir) throws IOException {
        File thumbnailFile = new File(dir, "s_" + file.getName());
        BufferedImage bo_image = Thumbnails.of(file).scale(1).asBufferedImage();
        BufferedImage bt_image = new BufferedImage(250, 250, BufferedImage.TYPE_INT_RGB); // BufferedImage의 타입 변경

        Graphics2D graphic = bt_image.createGraphics();

        // 그래픽 품질 향상을 위한 렌더링 힌트 설정
        graphic.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphic.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphic.drawImage(bo_image.getScaledInstance(250, 250, BufferedImage.SCALE_SMOOTH), 0, 0, 250, 250, null);

        // 섬네일을 JPEG 형식으로 저장하고 이미지 품질 조정
        ImageIO.write(bt_image, "jpeg", thumbnailFile); // 섬네일 포맷 변경 및 이미지 품질 조정

    }

    private void saveFileInfo(Images images, AlbumDto dto, File fileobj,MultipartFile file) throws IOException {
            ImagesFileInfo imageBoardFileInfo = new ImagesFileInfo();
            imageBoardFileInfo.setImages(images);
            String dirPath = File.separator + UPLOADPATH.UPPERDIRPATH + File.separator;
            dirPath += UPLOADPATH.IMAGEDIRPATH + File.separator + dto.getUsername() + File.separator
                    + dto.getSubCategory() + File.separator + images.getIamgeid();
            imageBoardFileInfo.setDir(dirPath);
            imageBoardFileInfo.setFilename(fileobj.getName());
            imageBoardFileInfo.setFileSize(file.getSize());
            BufferedImage image = Thumbnails.of(fileobj).scale(1).asBufferedImage();
            System.out.println("fileobj " + fileobj);
            System.out.println("image " + image);

            long width = image.getWidth();
            long height = image.getHeight();
            imageBoardFileInfo.setWidth(width);
            imageBoardFileInfo.setHeight(height);
            imageBoardFileInfo.setTool(dto.getTool());
            imagesFileInfoRepository.save(imageBoardFileInfo);
    }




    private void saveFileInfoMusic(Music music, AlbumDto dto, File fileobj) {

        MusicFileInfo musicFileInfo = new MusicFileInfo();
        musicFileInfo.setMusic(music);
        String dirPath = File.separator + UPLOADPATH.UPPERDIRPATH + File.separator;
        dirPath += UPLOADPATH.MUSICDIRPATH + File.separator + dto.getUsername() + File.separator
                + dto.getSubCategory() + File.separator + music.getMusicid();
        musicFileInfo.setDir(dirPath);
        musicFileInfo.setFilename(fileobj.getName());
        musicFileInfo.setAlbumImageName(dto.getImageFile().getOriginalFilename());
        musicFileInfoRepository.save(musicFileInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean uploadMusic(AlbumDto dto){
        return false;
    }




    //유저별 이미지 조회
    @Transactional(rollbackFor = Exception.class)
    public List<ImagesFileInfo> getUserItems() throws Exception {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails =  (PrincipalDetails) authentication.getPrincipal();
        String username =  principalDetails.getUsername();
        List<Images>  imagesList =  imagesRepository.findAllByUsername(username);
        List<ImagesFileInfo> myalbumImageList = new ArrayList<ImagesFileInfo>();

        for(Images el : imagesList){
            List<ImagesFileInfo> tmp =  imagesFileInfoRepository.findAllByImages(el);
            for(ImagesFileInfo el2 : tmp)
                myalbumImageList.add(el2);
        }

        return myalbumImageList;

    }
    //유저별 음악 조회
    @Transactional(rollbackFor = Exception.class)
    public List<MusicFileInfo> getUserMusicItems() throws Exception{
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails =  (PrincipalDetails) authentication.getPrincipal();
        String username =  principalDetails.getUsername();
        List<Music>  musicList =  musicRepository.findAllByUsername(username);
        List<MusicFileInfo> myalbumMusicList = new ArrayList();

        for(Music el : musicList){
            List<MusicFileInfo> tmp =  musicFileInfoRepository.findAllByMusic(el);
            for(MusicFileInfo el2 : tmp)
                myalbumMusicList.add(el2);
        }
        return myalbumMusicList;
    }




    @Transactional(rollbackFor = Exception.class)
    public List<ImagesFileInfo> getUserItem(Long imageid) throws Exception{
        Optional<Images> imagesOptional =  imagesRepository.findById(imageid);
        if(!imagesOptional.isEmpty())
            return imagesFileInfoRepository.findAllByImages(imagesOptional.get());
        return null;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MusicFileInfo> getUserMusicItem(Long musicid) {
        Optional<Music> musicOptional =  musicRepository.findById(musicid);
        if(!musicOptional.isEmpty())
            return musicFileInfoRepository.findAllByMusic(musicOptional.get());
        return null;
    }



    //모든 이미지 조회
    @Transactional(rollbackFor = Exception.class)
    public List<ImagesFileInfo> getAllItems() throws Exception{
        return imagesFileInfoRepository.findAll();
    }





    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User getUser(UserDto userDto) {
        if(userDto.getUsername()==null)
            return userRepository.findUserByNicknameAndPhone(userDto.getNickname(),userDto.getPhone());
        else
            return userRepository.findUserByUsernameAndNicknameAndPhone(userDto.getUsername(),userDto.getNickname(),userDto.getPhone());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeAlbumFile(Long fileid) {

        //삭제할 이미지파일정보
        ImagesFileInfo deleteImagesFileInfo =  imagesFileInfoRepository.findById(fileid).get();

        List<ImagesFileInfo> allSameImageIdFileInfo = imagesFileInfoRepository.findAllByImages(deleteImagesFileInfo.getImages());
        log.info("TOTAL SAME IMAGEID FILE COUNT : " + allSameImageIdFileInfo.size());
        if(allSameImageIdFileInfo.size()==1)    //마지막 삭제파일
        {
            imagesFileInfoRepository.deleteById(deleteImagesFileInfo.getFileid());
            imagesRepository.deleteById(deleteImagesFileInfo.getImages().getIamgeid());
            File file = new File(deleteImagesFileInfo.getDir()+File.separator+deleteImagesFileInfo.getFilename());
            file.delete();

            File thumb_file = new File(deleteImagesFileInfo.getDir()+File.separator+"s_"+deleteImagesFileInfo.getFilename());
            thumb_file.delete();

            File dir = new File(deleteImagesFileInfo.getDir());
            dir.delete();

            return true;
        }
        else
        {
            imagesFileInfoRepository.deleteById(deleteImagesFileInfo.getFileid());
            File file = new File(deleteImagesFileInfo.getDir()+File.separator+deleteImagesFileInfo.getFilename());
            file.delete();
            File thumb_file = new File(deleteImagesFileInfo.getDir()+File.separator+"s_"+deleteImagesFileInfo.getFilename());
            thumb_file.delete();

            return true;
        }



    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeAlbumMusicFile(Long fileid) {

        //삭제할 이미지파일정보
        MusicFileInfo deleteMusicFileInfo =  musicFileInfoRepository.findById(fileid).get();

        List<MusicFileInfo> allSameMusicIdFileInfo = musicFileInfoRepository.findAllByMusic(deleteMusicFileInfo.getMusic());
        log.info("TOTAL SAME IMAGEID FILE COUNT : " + allSameMusicIdFileInfo.size());
        if(allSameMusicIdFileInfo.size()==1)    //마지막 삭제파일
        {
            musicFileInfoRepository.deleteById(deleteMusicFileInfo.getFileid());
            imagesRepository.deleteById(deleteMusicFileInfo.getMusic().getMusicid());
            File file = new File(deleteMusicFileInfo.getDir()+File.separator+deleteMusicFileInfo.getFilename());
            file.delete();

            File thumb_file = new File(deleteMusicFileInfo.getDir()+File.separator+"s_"+deleteMusicFileInfo.getFilename());
            thumb_file.delete();

            File dir = new File(deleteMusicFileInfo.getDir());
            dir.delete();

            return true;
        }
        else
        {
            musicFileInfoRepository.deleteById(deleteMusicFileInfo.getFileid());
            File file = new File(deleteMusicFileInfo.getDir()+File.separator+deleteMusicFileInfo.getFilename());
            file.delete();
            File thumb_file = new File(deleteMusicFileInfo.getDir()+File.separator+"s_"+deleteMusicFileInfo.getFilename());
            thumb_file.delete();

            return true;
        }



    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeUser(UserDto userDto) {


        //  이미지 폴더 삭제
        String imageUploadPath= UPLOADPATH.ROOTDIRPATH+ File.separator+UPLOADPATH.UPPERDIRPATH+ File.separator;
        imageUploadPath+=UPLOADPATH.IMAGEDIRPATH+ File.separator+userDto.getUsername();

        File imageDir = new File(imageUploadPath);
        if(imageDir.exists())
            imageDir.delete();

        //  뮤직 폴더 삭제
        String musicUploadPath= UPLOADPATH.ROOTDIRPATH+ File.separator+UPLOADPATH.UPPERDIRPATH+ File.separator;
        musicUploadPath+=UPLOADPATH.MUSICDIRPATH+ File.separator+userDto.getUsername();

        File musicDir = new File(musicUploadPath);
        if(musicDir.exists())
            musicDir.delete();

        //DB 삭제
        imagesRepository.deleteAllByUsername(userDto.getUsername());
        musicRepository.deleteAllByUsername(userDto.getUsername());
        userRepository.deleteById(userDto.getUsername());
        //imagesFileInfoRepository.save(null);
        //musicFileInfoRepository.save(null);

        //AccessToken 만료시키기
        // cookie 에서 JWT token을 가져옵니다.
        Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME,null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmIdPw(String username, String password) {

        Optional<User> userOp =  userRepository.findById(username);
        if(userOp.isPresent()){
            User user = userOp.get();
           return  passwordEncoder.matches(password,user.getPassword());
        }
        return false;
    }




    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean modifiedMyInfo(UserDto userDto, Model model) {

        if(!StringUtils.equals(userDto.getPassword(),userDto.getRepassword())){
            model.addAttribute("password","패스워드가 일치하지 않습니다.");
            model.addAttribute("repassword","패스워드가 일치하지 않습니다.");
            return false;
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setNickname(userDto.getNickname());
        user.setPhone(userDto.getPhone());
        user.setZipcode(userDto.getZipcode());
        user.setAddr1(userDto.getAddr1());
        user.setAddr2(userDto.getAddr2());
        user.setRole("ROLE_USER");

        user.setBankname(userDto.getBankname());
        user.setAccount(userDto.getAccount());
        userRepository.save(user);

        return true;
    }

    //음악 앨범 추가
    @Override
    @Transactional(rollbackFor=Exception.class)
    public boolean uploadMusicAlbum(AlbumDto dto) {
        //Dto->ImagesEntity

        Music music = new Music();
        music.setMainCategory(dto.getMainCategory());
        music.setSubCategory(dto.getSubCategory());
        music.setTitle(dto.getTitle());
        music.setDescription(dto.getDescription());
        music.setUsername(dto.getUsername());
        music.setCreateAt(LocalDateTime.now());
        music.setLat(dto.getLat());
        music.setLng(dto.getLng());

        musicRepository.save(music);

        //저장 폴더 지정()
        String uploadPath = UPLOADPATH.ROOTDIRPATH + File.separator + UPLOADPATH.UPPERDIRPATH + File.separator;
        uploadPath += UPLOADPATH.MUSICDIRPATH + File.separator + dto.getUsername() + File.separator + dto.getSubCategory() + File.separator + music.getMusicid();

        File dir = new File(uploadPath);
        if (!dir.exists())
            dir.mkdirs();
        try {
            for (MultipartFile file : dto.getFiles()) {
                System.out.println("-----------------------------");
                System.out.println("filename : " + file.getName());
                System.out.println("filename(origin) : " + file.getOriginalFilename());
                System.out.println("filesize : " + file.getSize());
                System.out.println("-----------------------------");
                File fileobj = new File(dir, file.getOriginalFilename());    //파일객체생성


                file.transferTo(fileobj);   //저장

                // DB에 파일경로 저장
                saveFileInfoMusic(music, dto, fileobj);
            }
            //앨범이미지파일 추가
            File albumImageFile = new File(dir,dto.getImageFile().getOriginalFilename());
            dto.getImageFile().transferTo(albumImageFile);


        }catch(IOException e){
            e.printStackTrace();
        }
        return true;

    }


}
