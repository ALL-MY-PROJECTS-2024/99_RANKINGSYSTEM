package com.creator.imageAndMusic.domain.service;


import com.creator.imageAndMusic.config.auth.PrincipalDetails;
import com.creator.imageAndMusic.config.auth.jwt.JwtTokenProvider;
import com.creator.imageAndMusic.domain.dto.AlbumDto;
import com.creator.imageAndMusic.domain.dto.UserDto;
import com.creator.imageAndMusic.domain.entity.Images;
import com.creator.imageAndMusic.domain.entity.ImagesFileInfo;
import com.creator.imageAndMusic.domain.entity.User;
import com.creator.imageAndMusic.domain.repository.ImagesFileInfoRepository;
import com.creator.imageAndMusic.domain.repository.ImagesRepository;
import com.creator.imageAndMusic.domain.repository.UserRepository;
import com.creator.imageAndMusic.properties.AUTH;
import com.creator.imageAndMusic.properties.UPLOADPATH;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    private ImagesFileInfoRepository imagesFileInfoRepository;


    @Transactional(rollbackFor = Exception.class)
    public boolean memberJoin(UserDto dto, Model model, HttpServletRequest request) throws Exception{

        //비지니스 Validation Check

        //password vs repassword 일치여부
        if(!dto.getPassword().equals(dto.getRepassword()) ){
            model.addAttribute("password","패스워드 입력이 상이합니다 다시 입력하세요");
            System.out.println("UserServiceImpl's memberJoin .. 패스워드 불일치");
            return false;
        }

        //동일 계정이 있는지 여부 확인
        if(userRepository.existsById(dto.getUsername())){
            model.addAttribute("username","동일한 계정명이 존재합니다.");
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
            model.addAttribute("username","이메일 인증 유효시간을 초과했습니다");
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
                model.addAttribute("username","해당 계정의 이메일 인증이 되어있지 않습니다.");
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
            saveFileInfo(images, dto, fileobj);


        }

        return true;
    }
    private void createThumbnail(File file, File dir) throws IOException {
        File thumbnailFile = new File(dir, "s_" + file.getName());
        BufferedImage bo_image = ImageIO.read(file);
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

    private void saveFileInfo(Images images, AlbumDto dto, File fileobj) {
        ImagesFileInfo imageBoardFileInfo = new ImagesFileInfo();
        imageBoardFileInfo.setImages(images);
        String dirPath = File.separator + UPLOADPATH.UPPERDIRPATH + File.separator;
        dirPath += UPLOADPATH.IMAGEDIRPATH + File.separator + dto.getUsername() + File.separator
                + dto.getSubCategory() + File.separator + images.getIamgeid();
        imageBoardFileInfo.setDir(dirPath);
        imageBoardFileInfo.setFilename(fileobj.getName());
        imagesFileInfoRepository.save(imageBoardFileInfo);
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
    @Transactional(rollbackFor = Exception.class)
    public List<ImagesFileInfo> getUserItem(Long imageid) throws Exception{
        Optional<Images> imagesOptional =  imagesRepository.findById(imageid);
        if(!imagesOptional.isEmpty())
            return imagesFileInfoRepository.findAllByImages(imagesOptional.get());
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

}
