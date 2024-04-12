package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.config.auth.jwt.JwtTokenProvider;
import com.creator.imageAndMusic.config.auth.jwt.TokenInfo;
import com.creator.imageAndMusic.domain.dto.AlbumDto;
import com.creator.imageAndMusic.domain.dto.UserDto;
import com.creator.imageAndMusic.domain.entity.Images;
import com.creator.imageAndMusic.domain.entity.ImagesFileInfo;
import com.creator.imageAndMusic.domain.entity.User;
import com.creator.imageAndMusic.domain.repository.UserRepository;
import com.creator.imageAndMusic.domain.service.UserService;
import com.creator.imageAndMusic.properties.AUTH;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.text.html.HTML;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


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


    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @GetMapping("/join")
    public void join(){
        log.info("GET /user/join...");

    }

    @ExceptionHandler(Exception.class)
    public void ExceptionHandler(Exception e){
        log.info("User Exception.." + e);
    }


    //ID찾기
    @GetMapping("/confirmId")
    public void confirmId(){
        log.info("GET /user/confirmId..");
    }


    @PostMapping("/confirmId")
    public @ResponseBody ResponseEntity<String> confirmId_post(@RequestBody UserDto userDto){
        log.info("POST /user/confirmId.." + userDto);

        User user =  userService.getUser(userDto);

        if(user!=null){
            String username = user.getUsername();
            username = username.substring(0, username.indexOf("@")-2);
            username = username+"**";
            log.info("USERNAME : " + username);
            return new ResponseEntity(username, HttpStatus.OK);
        }
        else{
            return new ResponseEntity("일치하는 계정을 찾을수 없습니다.", HttpStatus.BAD_GATEWAY);
        }

    }

    //PW찾기
    @GetMapping("/confirmPw")
    public void confirmPw(){

        log.info("GET /user/confirmPw..");
    }

    @Autowired
    private UserRepository userRepository;
    @PostMapping("/confirmPw")
    public @ResponseBody ResponseEntity<String> confirmPw_post(@RequestBody UserDto userDto){
        log.info("POST /user/confirmPw.." + userDto);

        User user =  userService.getUser(userDto);

        if(user!=null){

            //난수 패스워드
            Random rand =new Random();
            int value = (int)(rand.nextDouble()*100000) ;

            String rowPassword = user.getPassword();

            //DB저장
            user.setPassword(passwordEncoder.encode(String.valueOf(value)));
            userRepository.save(user);

            //이메일 발송
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getUsername());
            message.setSubject("[RANKING WEB SERVICE] 임시 패스워드 ");
            message.setText(value+"");
            javaMailSender.send(message);





            return new ResponseEntity(user.getUsername()+" 으로 임시 패스워드 전송 완료",HttpStatus.OK);
        }
        else{
            return new ResponseEntity("일치하는 계정을 찾을수 없습니다.", HttpStatus.BAD_GATEWAY);
        }

    }


    @PostMapping("/join")
    public String join_post(@Valid UserDto dto, BindingResult bindingResult, Model model, HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes) throws Exception {
        UserController.log.info("POST /join...dto " + dto);
        //파라미터 받기
            //
        //입력값 검증(유효성체크)
        //System.out.println(bindingResult);
        if(bindingResult.hasFieldErrors()){
            for(FieldError error :bindingResult.getFieldErrors()){
                log.info(error.getField() +" : " + error.getDefaultMessage());
                model.addAttribute(error.getField(),error.getDefaultMessage());
            }
            return "/";
        }

        //서비스 실행

        boolean isJoin =  userService.memberJoin(dto,model,request);
        log.info("isJoin : "+isJoin);
        //View로 속성등등 전달
        if(isJoin) {

            //JWT 토큰 쿠키 삭제후 response 전송
            Cookie newCookie = new Cookie(AUTH.EMAIL_COOKIE_NAME,null);

            newCookie.setMaxAge(0);
            newCookie.setPath("/");
            response.addCookie(newCookie);
            redirectAttributes.addAttribute("message","JOIN SUCCESS");
            return "redirect:login";
        }
        else {
            return "/";
            //+a 예외처리
        }

    }


    @GetMapping("/sendMail/{email}")
    @ResponseBody
    public ResponseEntity<JSONObject> sendmailFunc(@PathVariable("email") String email, HttpServletResponse response){
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
        TokenInfo tokenInfo =  jwtTokenProvider.generateToken(AUTH.EMAIL_COOKIE_NAME,value+"",false,email);
        Cookie cookie  = new Cookie(AUTH.EMAIL_COOKIE_NAME,tokenInfo.getAccessToken());
        cookie.setPath("/");
        cookie.setMaxAge(60*15);
        response.addCookie(cookie);

        return new ResponseEntity(new JSONObject().put("success", true) , HttpStatus.OK);
    }

    @GetMapping("/emailConfirm")
    public @ResponseBody JSONObject emailConfirmFunc(@RequestParam(value = "emailCode" ,defaultValue = "0") String emailCode,HttpServletRequest request , HttpServletResponse response){
        UserController.log.info("GET /user/emailConfirm... code : " + emailCode);




        //JWT 토큰 쿠키중에 Email인증 토큰 쿠키 찾기
        Cookie c =  Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().startsWith(AUTH.EMAIL_COOKIE_NAME)).findFirst().orElse(null);

        if(c==null)
            return null;


        System.out.println(c.getName() + " | " + c.getValue());

        //Claims 꺼내기
        Claims claims = jwtTokenProvider.parseClaims(c.getValue());
        String idValue = (String) claims.get("id");
        String username = (String) claims.get("username");
        boolean isAuth = (Boolean) claims.get(AUTH.EMAIL_COOKIE_NAME);

        System.out.println("claims : " + claims);
        System.out.println("idValue : " + idValue);
        System.out.println("username : " + username);
        System.out.println("isAuth : " + isAuth);

        JSONObject obj = new JSONObject();

        if(!isAuth) //email 전송은 완료 ,But 코드 입력 아직 안함
        {

            if(idValue.equals(emailCode)){

                //토큰 쿠키를 true로 만들어야함(아직)

                //기존 쿠키 만료
                c.setMaxAge(0);
                response.addCookie(c);

                //true 값 가지는 쿠키 다시 만들어서 전달
                TokenInfo tokenInfo =  jwtTokenProvider.generateToken(AUTH.EMAIL_COOKIE_NAME,"",true,username);
                Cookie cookie  = new Cookie(AUTH.EMAIL_COOKIE_NAME,tokenInfo.getAccessToken());
                cookie.setPath("/");
                cookie.setMaxAge(60*15);
                response.addCookie(cookie);

                obj.put("success",true);
                obj.put("username",username);
                obj.put("message","이메일 인증을 성공하셨습니다.");
                System.out.println("!!!!!!!!!!!!!!!!!!!!!" +username);
                return obj;
            }
            else {

                //받은 이메일코드랑 다르면
                obj.put("success",false);
                obj.put("message","이메일 인증을 실패했습니다.");
                return obj;

            }

        }
        else //코드 입력 완료
        {
            obj.put("success",true);
            obj.put("username",username);
            obj.put("message","이메일 인증을 성공하셨습니다.");
            return obj;

        }



    }



    @GetMapping("/myinfo")
    public void func1(){
          log.info("GET /user/myinfo..");

    }


    @GetMapping("/album/main")
    public void func2(Model model) throws Exception {

        log.info("GET /user/album/main...");
        List<ImagesFileInfo> list =  userService.getUserItems();



        model.addAttribute("list",list);

    }


    @GetMapping("/album/add")
    public void func3(){
        log.info("GET /album/add");
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


    @GetMapping("/album/read")
    public void read_album(@RequestParam(name = "iamgeid") Long iamgeid,Model model) throws Exception {

        log.info("GET /user/album/read...iamgeid " + iamgeid);

        List<ImagesFileInfo> filelist =  userService.getUserItem(iamgeid);
        Images images =  filelist.get(0).getImages();

        model.addAttribute("filelist",filelist);
        model.addAttribute("images",images);
    }

    @DeleteMapping("/album/delete")
    public @ResponseBody ResponseEntity<String> delete_album(@RequestParam(name = "fileid") Long fileid){
        log.info("GET /user/album/delete...fileid " + fileid);

        boolean isDeleted =  userService.removeAlbumFile(fileid);
        if(isDeleted)
           return  new ResponseEntity("삭제완료,앨범 메인 페이지로 이동하시겠습니까?",HttpStatus.OK);
        else
           return  new ResponseEntity("삭제실패",HttpStatus.BAD_GATEWAY);

    }



}
