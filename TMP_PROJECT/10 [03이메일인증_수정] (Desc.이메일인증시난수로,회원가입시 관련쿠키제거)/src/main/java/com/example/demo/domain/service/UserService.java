package com.example.demo.domain.service;


import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.properties.EmailAuthProperties;
import com.example.demo.properties.JoinAuthProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpServletResponse response;

    public boolean memberJoin(UserDto dto){

        //비지니스 Validation Check

        //Dto->Entity
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setRole("ROLE_USER");

        //Db Saved...
        userRepository.save(user);


        //쿠키에서 ImportAuth 제거
        Cookie[] cookies =  request.getCookies();

        Cookie importAuthcookie =  Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(JoinAuthProperties.COOKIE_NAME)).findFirst().orElse(null);
        System.out.println("UserService memberJoin().. removed cookies :" + importAuthcookie.getName() + " | " + importAuthcookie.getValue());
        importAuthcookie.setMaxAge(0);
        importAuthcookie.setPath("/");
        response.addCookie(importAuthcookie);

        //쿠키에서 EmailAuth 제거
        Cookie emailAuthcookie =  Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(EmailAuthProperties.COOKIE_NAME)).findFirst().orElse(null);
        System.out.println("UserService memberJoin().. removed cookies :" + emailAuthcookie.getName() + " | " +  emailAuthcookie.getValue());
        emailAuthcookie.setMaxAge(0);
        emailAuthcookie.setPath("/");
        response.addCookie(emailAuthcookie);


        return userRepository.existsById(user.getUsername());
    }


}
