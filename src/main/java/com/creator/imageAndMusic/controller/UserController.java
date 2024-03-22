package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.domain.dto.AlbumDto;
import com.creator.imageAndMusic.domain.dto.UserDto;
import com.creator.imageAndMusic.domain.entity.ImagesFileInfo;
import com.creator.imageAndMusic.domain.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;

    @GetMapping("/join")
    public void join(){
        log.info("GET /user/join...");

    }


    @ExceptionHandler(Exception.class)
    public void ExceptionHandler(Exception e){
        log.info("User Exception.." + e);
    }
    @PostMapping("/join")
    public String join_post(@Valid UserDto dto, BindingResult bindingResult, Model model, HttpServletRequest request) throws Exception {
        UserController.log.info("POST /join...dto " + dto);
        //파라미터 받기

        //입력값 검증(유효성체크)
        //System.out.println(bindingResult);
        if(bindingResult.hasFieldErrors()){
            for(FieldError error :bindingResult.getFieldErrors()){
                log.info(error.getField() +" : " + error.getDefaultMessage());
                model.addAttribute(error.getField(),error.getDefaultMessage());
            }
            return "user/join";
        }

        //서비스 실행

        boolean isJoin =  userService.memberJoin(dto,model,request);
        //View로 속성등등 전달
        if(isJoin)
            return "redirect:login?msg=MemberJoin Success!";
        else
            return "user/join";
        //+a 예외처리

    }


    @GetMapping("/myinfo")
    public void func1(){}


    @GetMapping("/album/main")
    public void func2(Model model) throws Exception {

        log.info("GET /user/album/main...");
        List<ImagesFileInfo> list =  userService.getAllItems();



        model.addAttribute("list",list);

    }


    @GetMapping("/album/add")
    public void func3(){


    }

    @PostMapping("/album/add")
    public  @ResponseBody void add_image(AlbumDto dto) throws IOException {
        log.info("POST /album/add : " + dto+" file count : " + dto.getFiles().length);
        //유효성 검사
//        if(bindingResult.hasFieldErrors()){
//            for(FieldError error :bindingResult.getFieldErrors()){
//                log.info(error.getField() +" : " + error.getDefaultMessage());
//                //model.addAttribute(error.getField(),error.getDefaultMessage());
//            }
//        }

        //서비스 실행
        boolean isUploaded =  userService.uploadAlbum(dto);


    }


}
