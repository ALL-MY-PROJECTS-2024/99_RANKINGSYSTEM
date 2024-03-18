package com.example.demo.controller;

import com.example.demo.config.auth.jwt.JwtTokenProvider;
import com.example.demo.config.auth.jwt.TokenInfo;
import com.example.demo.domain.dto.CertificationDto;
import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.service.UserService;
import com.example.demo.properties.EmailAuthProperties;
import com.example.demo.properties.JoinAuthProperties;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;


    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @InitBinder
    public void dataBinder(WebDataBinder dataBinder){
        log.info("databinder obj : " + dataBinder);
        //dataBinder.registerCustomEditor(String.class,new PhoneNumberEditor());
        dataBinder.registerCustomEditor(String.class,"phone" ,new PhoneNumberEditor());
    }



    @GetMapping(value = "/myinfo" )
    public void user(Authentication authentication , Model model){
        UserController.log.info("GET /user/myinfo...Authentication : " + authentication);
        UserController.log.info("username : " + authentication.getName());
        UserController.log.info("principal : " + authentication.getPrincipal());
        UserController.log.info("authorities : " + authentication.getAuthorities());
        UserController.log.info("details :  " +authentication.getDetails());
        UserController.log.info("credentials : " + authentication.getCredentials());

        model.addAttribute("authentication",authentication);

    }



    @GetMapping("/join")
    public String join(HttpServletRequest request){

        UserController.log.info("GET /join");

        //JWT JOIN_AUTH 토큰 검증
        Cookie c =  Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(JoinAuthProperties.COOKIE_NAME)).findFirst().orElse(null);
        System.out.println(c.getName() + " | " + c.getValue());

        //생략!(AutorizationFilter에서 이미 토큰 ValidationCheck완료)
        //jwtTokenProvider.validateToken(c.getValue());

        //Claims 꺼내기
        Claims claims = jwtTokenProvider.parseClaims(c.getValue());
        boolean isAuthenticated =  (boolean)claims.get(JoinAuthProperties.TOKEN_ID);
        if(!isAuthenticated)
            return "user/certification";

        return "user/join";
    }



    @PostMapping("/join")
    public String join_post(@Valid UserDto dto, BindingResult bindingResult,Model model){
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
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        boolean isJoin =  userService.memberJoin(dto);
        //View로 속성등등 전달
        if(isJoin)
            return "login?msg=MemberJoin Success!";
        else
            return "forward:join?msg=Join Failed....";
        //+a 예외처리

    }

    @GetMapping("/certification")
    public String certification(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserController.log.info("GET /user/certification");

        if(request.getCookies() !=null) {
            boolean isExisted = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(JoinAuthProperties.COOKIE_NAME)).findFirst()
                    .isEmpty();
            if (!isExisted) {
                response.sendRedirect("/user/join");
                return null;
            }
        }
        return "user/certification";
    }



    @GetMapping("/findId")
    public void findId(){
        UserController.log.info("GET /user/findId");}
    @GetMapping("/findPw")
    public void findPw(){
        UserController.log.info("GET /user/findPw");}



    //---------------------
    @PostMapping(value = "/certification",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONObject>  certification_post(@RequestBody CertificationDto params, HttpServletResponse response) throws IOException {
        UserController.log.info("POST /user/certification.." + params);

        //쿠키로 본인인증 완료값을 전달!(수정)
        TokenInfo tokenInfo =  jwtTokenProvider.generateToken(JoinAuthProperties.COOKIE_NAME, JoinAuthProperties.TOKEN_ID,true);
        Cookie authCookie = new Cookie(JoinAuthProperties.COOKIE_NAME,tokenInfo.getAccessToken());
        authCookie.setMaxAge(60*30); //30분동안 유지
        authCookie.setPath("/");
        response.addCookie(authCookie);

        JSONObject obj = new JSONObject();
        obj.put("success",true);

        return  new ResponseEntity<JSONObject>(obj, HttpStatus.OK);

    }
    @GetMapping("/sendMail/{email}")
    @ResponseBody
    public ResponseEntity<JSONObject> sendmailFunc(@PathVariable("email") String email,HttpServletResponse response){
        UserController.log.info("GET /user/sendMail.." + email);
        //넣을 값 지정
        //메일 메시지 만들기
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[EMAIL AUTHENTICATION] CODE ");
        //난수코드 전달로 변경
        Random rand =new Random();
        int value = (int)(rand.nextDouble()*100000) ;

        message.setText(value+"");
        javaMailSender.send(message);


        //Token에 난수Value전달
        TokenInfo tokenInfo =  jwtTokenProvider.generateToken(EmailAuthProperties.COOKIE_NAME,value+"",true);
        Cookie cookie  = new Cookie(EmailAuthProperties.COOKIE_NAME,tokenInfo.getAccessToken());
        cookie.setPath("/");
        cookie.setMaxAge(60*15);
        response.addCookie(cookie);





        return new ResponseEntity(new JSONObject().put("success", true) , HttpStatus.OK);
    }

    @GetMapping("/emailConfirm")
    public @ResponseBody JSONObject emailConfirmFunc(String emailCode,HttpServletRequest request , HttpServletResponse response){
        UserController.log.info("GET /user/emailConfirm... code : " + emailCode);




        //JWT 토큰 쿠키중에 Email인증 토큰 쿠키 찾기
        Cookie c =  Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(EmailAuthProperties.COOKIE_NAME)).findFirst().orElse(null);

        System.out.println(c.getName() + " | " + c.getValue());

        //Claims 꺼내기
        Claims claims = jwtTokenProvider.parseClaims(c.getValue());
        String idValue = (String) claims.get("id");
        boolean isAuth = (Boolean) claims.get(EmailAuthProperties.COOKIE_NAME);


        JSONObject obj = new JSONObject();

        if(isAuth==true && idValue.equals(emailCode)) {

            obj.put("success",true);
            obj.put("message","이메일 인증을 성공하셨습니다.");
            return obj;
        }



        obj.put("success",false);
        obj.put("message","이메일 인증을 실패했습니다.");
        return obj;
    }


}



class PhoneNumberEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        System.out.println("PhoneNumberEditor's setAsText()..text : " + getValue());
        return (String)getValue();
    }
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        System.out.println("PhoneNumberEditor's setAsText()..text : " + text);
        String formattedText =  text.replaceAll("-","");
        setValue(formattedText);
    }
}

