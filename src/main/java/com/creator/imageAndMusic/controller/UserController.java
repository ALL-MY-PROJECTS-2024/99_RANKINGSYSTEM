package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.config.auth.PrincipalDetails;
import com.creator.imageAndMusic.config.auth.jwt.JwtProperties;
import com.creator.imageAndMusic.config.auth.jwt.JwtTokenProvider;
import com.creator.imageAndMusic.config.auth.jwt.TokenInfo;
import com.creator.imageAndMusic.domain.dto.AlbumDto;
import com.creator.imageAndMusic.domain.dto.UserDto;
import com.creator.imageAndMusic.domain.entity.*;
import com.creator.imageAndMusic.domain.repository.UserRepository;
import com.creator.imageAndMusic.domain.service.TradingImageServiceImpl;
import com.creator.imageAndMusic.domain.service.TradingMusicServiceImpl;
import com.creator.imageAndMusic.domain.service.UserService;
import com.creator.imageAndMusic.properties.AUTH;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;


@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {


    //https://hyphen.im/product-api/view?seq=31

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;


    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private TradingImageServiceImpl tradingImageService;

    @Autowired
    private TradingMusicServiceImpl tradingMusicService;
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

    //https://hyphen.im/product-api/view?seq=31
    @PostMapping("/comfirm/account")
    public @ResponseBody ResponseEntity<Map<String,Object>> confirm_account(
            @RequestParam("bankCode") String bankCode,
            @RequestParam("account") String account
    ){
        Map<String,Object> result = new HashMap<String,Object>();

        //URL
        String url="https://api.hyphen.im/hb0081000378";

        //HEADER
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json");
        headers.add("hyphen-gustation","Y");
        headers.add("user-id","jwg8910");
        headers.add("Hkey","97036f5338c9a0f4");

        //PARAM
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        JSONObject obj = new JSONObject();
        obj.put("inBankCode",bankCode);
        obj.put("inAccount",account);

        //ENTITY(HEADER + PARAM)
        HttpEntity< JSONObject > entity = new HttpEntity(obj,headers);
        //REQUEST
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response =rt.exchange(url, HttpMethod.POST,entity,String.class);
        System.out.println(response.getBody());

        return new ResponseEntity<>(result,HttpStatus.OK);
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
    public  @ResponseBody  ResponseEntity<JSONObject> join_post(@Valid UserDto dto, BindingResult bindingResult, Model model, HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes) throws Exception {
        UserController.log.info("POST /join...dto " + dto);
        //파라미터 받기
            //
        JSONObject jsonObject = new JSONObject();


       Optional<User> userOptional =  userRepository.findById(dto.getUsername());
       if(userOptional.isPresent()){
           jsonObject.put("message","동일 계정이 존재합니다.");
           jsonObject.put("type","user");

           return new ResponseEntity<>(jsonObject,HttpStatus.BAD_GATEWAY);
       }

        //입력값 검증(유효성체크)
        //System.out.println(bindingResult);
        if(bindingResult.hasFieldErrors()){
            for(FieldError error :bindingResult.getFieldErrors()){
                log.info(error.getField() +" : " + error.getDefaultMessage());
                model.addAttribute(error.getField(),error.getDefaultMessage());
                jsonObject.put(error.getField(),error.getDefaultMessage());
            }
            jsonObject.put("type","validation");
            jsonObject.put("message","잘못입력된 항목이 존재합니다.\n확인해보세요.");
            return new ResponseEntity<>(jsonObject,HttpStatus.BAD_GATEWAY);
        }

        //서비스 실행
        boolean isJoin =  userService.memberJoin(dto,model,request);
        log.info("isJoin : "+isJoin);
        //View로 속성등등 전달
        if(isJoin) {
            jsonObject.put("type","isJoin");
            jsonObject.put("isJoin",true);

            //JWT 토큰 쿠키 삭제후 response 전송
            Cookie newCookie = new Cookie(AUTH.EMAIL_COOKIE_NAME,null);
            newCookie.setMaxAge(0);
            newCookie.setPath("/");
            response.addCookie(newCookie);
            return new ResponseEntity<>(jsonObject,HttpStatus.OK);
        }
        else {
            jsonObject.put("type","isJoin");
            jsonObject.put("isJoin",true);
            String message = (String)model.getAttribute(("message"));
            jsonObject.put("message",message);
            return new ResponseEntity<>(jsonObject,HttpStatus.BAD_GATEWAY);
        }

    }


    @GetMapping("/sendMail/{email}")
    @ResponseBody
    public ResponseEntity<JSONObject> sendmailFunc(@PathVariable("email") String email, HttpServletResponse response){
        UserController.log.info("GET /user/sendMail.." + email);

        JSONObject result = new JSONObject();

        //1 가입된 계정인지 확인
        Optional<User> userOptional =  userRepository.findById(email);
        if(userOptional.isPresent()){
            result.put("isSuccess",false);
            result.put("message","동일 계정이 존재합니다.");
            return new ResponseEntity<>(result , HttpStatus.OK);

        }else{
            //메일 메시지 만들기
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("[WEB발신] 이메일인증코드 발송 ");
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

            result.put("isSuccess",true);
            result.put("message","인증코드 발송을 완료하였습니다.");
            return new ResponseEntity<>(result , HttpStatus.OK);

        }


    }

    @GetMapping("/emailConfirm")
    public @ResponseBody JSONObject emailConfirmFunc(@RequestParam(value = "emailCode" ,defaultValue = "0") String emailCode,HttpServletRequest request , HttpServletResponse response){
        UserController.log.info("GET /user/emailConfirm... code : " + emailCode);

        JSONObject result = new JSONObject();

        //JWT 토큰 쿠키중에 Email인증 토큰 쿠키 찾기
        Cookie c =  Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().startsWith(AUTH.EMAIL_COOKIE_NAME)).findFirst().orElse(null);

        if(c==null){
            result.put("message","유효한 이메일주소를 입력한 뒤 인증버튼을 클릭해주세요");
            return result;
        }



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
                return obj;
            }
            else {

                //받은 이메일코드랑 다르면
                obj.put("success",false);
                obj.put("message","이메일 인증코드값이 잘못입력되었습니다.");
                return obj;

            }

        }
        else //코드 입력 완료
        {
            obj.put("success",true);
            obj.put("username",username);
            obj.put("message","이메일 인증을 성공!.");
            return obj;

        }



    }





    @GetMapping("/album/main")
    public void func2(Model model) throws Exception {

        log.info("GET /user/album/main...");
        //전체 보유 이미지(제한 걸어야되는데..)
        List<ImagesFileInfo> list =  userService.getUserItems();
        model.addAttribute("list",list);

//        //전체 보유 음악(제한 걸어야되는데..)
        List<MusicFileInfo> musicList =  userService.getUserMusicItems();
        model.addAttribute("musicList",musicList);

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
    @PostMapping("/music/add")
    public String add_music(AlbumDto dto) throws IOException {
        System.out.println("POST /music/add : " + dto+" file count : " + dto.getFiles().length);
        //서비스 실행

        boolean isUploaded = userService.uploadMusicAlbum(dto);

        if(isUploaded){
            return "redirect:/user/album/main";
        }
        return "user/album/add";
    }

    @GetMapping("/album/read")
    public void read_album(@RequestParam(name = "iamgeid") Long iamgeid,Model model) throws Exception {

        log.info("GET /user/album/read...iamgeid " + iamgeid);

        List<ImagesFileInfo> filelist =  userService.getUserItem(iamgeid);
        Images images =  filelist.get(0).getImages();

        model.addAttribute("filelist",filelist);
        model.addAttribute("images",images);
    }
    @GetMapping("/album/readmusic")
    public void read_album_music(@RequestParam(name = "musicid") Long musicid,Model model) throws Exception {

        log.info("GET /user/album/readmusic...musicid " + musicid);

        List<MusicFileInfo> filelist =  userService.getUserMusicItem(musicid);
        Music music =  filelist.get(0).getMusic();

        model.addAttribute("filelist",filelist);
        model.addAttribute("music",music);
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


    @DeleteMapping("/album/musicdelete")
    public @ResponseBody ResponseEntity<String> delete_albu_music(@RequestParam(name = "fileid") Long fileid){
        log.info("GET /user/album/musicdelete...fileid " + fileid);

        boolean isDeleted =  userService.removeAlbumMusicFile(fileid);
        if(isDeleted)
            return  new ResponseEntity("삭제완료,앨범 메인 페이지로 이동하시겠습니까?",HttpStatus.OK);
        else
            return  new ResponseEntity("삭제실패",HttpStatus.BAD_GATEWAY);

    }


//----------------------------------------------------------------
//  MYINFO
//----------------------------------------------------------------

    private boolean isconfirmed = false;
    private boolean ispasswordOk = false;
    @GetMapping("/myinfo/read")
    public String func1(@AuthenticationPrincipal PrincipalDetails principalDetails,Model model){

        if(!isconfirmed)
            return "redirect:/user/myinfo/confirm";
        UserDto userDto =  principalDetails.getUserDto();

        model.addAttribute("dto",userDto);

        log.info("GET /user/myinfo/read..dto" + userDto);
        return "user/myinfo/read";

    }

    @GetMapping("/myinfo/update")
    public String update(@AuthenticationPrincipal PrincipalDetails principalDetails,Model model){
        if(!isconfirmed)
            return "redirect:/user/myinfo/confirm";

        UserDto userDto =  principalDetails.getUserDto();
        model.addAttribute("dto",userDto);
        log.info("GET /user/myinfo/update..");
        ispasswordOk = false;
        return "user/myinfo/update";

    }
    @PostMapping("/myinfo/update")
    public String update_post(@ModelAttribute @Valid UserDto userDto, BindingResult bindingResult, Authentication authentication,HttpServletResponse response, Model model){
        log.info("POST /user/myinfo/update..userDto" + userDto);

        if(!ispasswordOk){
            model.addAttribute("oldpassword","패스워드 확인을 하지 않았습니다");
            model.addAttribute("dto",userDto);
            return "user/myinfo/update";
        }

        if(bindingResult.hasFieldErrors()){
            for(FieldError error :bindingResult.getFieldErrors()){
                log.info(error.getField() +" : " + error.getDefaultMessage());
                model.addAttribute(error.getField(),error.getDefaultMessage());
            }
            model.addAttribute("dto",userDto);
            return "user/myinfo/update";
        }

        boolean isUpdated =  userService.modifiedMyInfo(userDto,model);
        if(!isUpdated){
            model.addAttribute("dto",userDto);
            return "user/myinfo/update";
        }
        else{
            //--------------------------------------
            //JWT TOKEN 변경 적용
            //--------------------------------------
            PrincipalDetails principalDetails = new PrincipalDetails();
            principalDetails.setUserDto(userDto);

            PrincipalDetails old = (PrincipalDetails)authentication.getPrincipal();
            principalDetails.setAccessToken(old.getAccessToken());   //Oauth AccessToken

            //JWT + NO REMEMBERME
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(principalDetails, authentication.getCredentials(), authentication.getAuthorities());

            TokenInfo tokenInfo = jwtTokenProvider.generateToken(usernamePasswordAuthenticationToken);
            // 쿠키 생성
            Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, tokenInfo.getAccessToken());
            cookie.setMaxAge(JwtProperties.EXPIRATION_TIME); // 쿠키의 만료시간 설정
            cookie.setPath("/");
            response.addCookie(cookie);

            //AUthentication 다시
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            //--------------------------------------
        }

        return "redirect:/user/myinfo/read";

    }
    @PostMapping("/myinfo/confirmPw")
    public @ResponseBody ResponseEntity<String> confirm_pw(@RequestParam("password") String password,@AuthenticationPrincipal PrincipalDetails principalDetails){
        log.info("POST /user/myinfo/confirmPw..password : " + password);
        UserDto userDto =  principalDetails.getUserDto();

        boolean isOk = userService.confirmIdPw(userDto.getUsername(),password);
        if(isOk){
            ispasswordOk = true;
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_GATEWAY);

    }
    //계정삭제
    @GetMapping("/myinfo/delete")
    public void delete(){
        log.info("GET /user/myinfo/delete..");
    }

    @DeleteMapping("/myinfo/delete")
    public @ResponseBody ResponseEntity<String> delete_del(@RequestParam("password") String password, @AuthenticationPrincipal PrincipalDetails principalDetails){
        log.info("DELETE /user/myinfo/delete..");


        UserDto userDto =  principalDetails.getUserDto();

        boolean isOk = userService.confirmIdPw(userDto.getUsername(),password);
        boolean isdeleted = false;
        if(isOk){
            isdeleted = userService.removeUser(userDto);
        }

        if(!isdeleted)
            return new ResponseEntity("fail",HttpStatus.BAD_GATEWAY);

        return new ResponseEntity("success",HttpStatus.OK);
    }


    @GetMapping("/myinfo/confirm")
    public void myinfoConfirm(){
        log.info("GET /user/myinfo/confirm");
    }
    @PostMapping("/myinfo/confirm")
    public String myinfoConfirm_post(@RequestParam("password") String password , @AuthenticationPrincipal PrincipalDetails principalDetails,Model model){
        log.info("GET /user/myinfo/confirm");
        String username = principalDetails.getUserDto().getUsername();
        boolean isok =  userService.confirmIdPw(username,password);
        if(isok){
            isconfirmed = true;
            return "redirect:/user/myinfo/read";
        }
        isconfirmed = false;
        model.addAttribute("icon","warning");
        model.addAttribute("message","패스워드가 일치하지 않습니다.");
        return "user/myinfo/confirm";
    }


    @GetMapping("/image/upload")
    public void f(){
        log.info("");
    }




    //----------------------------------------------------------------  
    // 즐겨찾기
    //----------------------------------------------------------------
    @GetMapping("/bookmark")
    public void favorite(){
        log.info("GET /user/bookmark/my");

    }

    //----------------------------------------------------------------
    // 경매상태 확인
    //----------------------------------------------------------------
    @GetMapping("/myinfo/trading/main")
    public void trade(@AuthenticationPrincipal PrincipalDetails principalDetails,Model model){
        log.info("GET /user/myinfo/trading");
        String seller =  principalDetails.getUserDto().getUsername();
        List<TradingImage> list =  tradingImageService.getMyTradingImage(seller);
        List<TradingMusic> musicList = tradingMusicService.getMyTradingMusic(seller);

        model.addAttribute("list",list);
        model.addAttribute("musicList",musicList);
    }
    //----------------------------------------------------------------
    // 낙찰받은 경매
    //----------------------------------------------------------------
    @GetMapping("/myinfo/trading/auctioned")
    public void auctioned(@AuthenticationPrincipal PrincipalDetails principalDetails,Model model){
        log.info("GET /user/myinfo/trading/auctioned");
        String buyer =  principalDetails.getUserDto().getUsername();
        List<TradingImage> list =  tradingImageService.getMyAuctionedImage(buyer);
        List<TradingMusic> musicList = tradingMusicService.getMyAuctionedMusic(buyer);

        model.addAttribute("list",list);
        model.addAttribute("musicList",musicList);
        model.addAttribute("userDto",principalDetails.getUserDto());
    }
    //----------------------------------------------------------------
    // 경매 취소 - 삭제
    //----------------------------------------------------------------
    @GetMapping("/myinfo/trading/del")
    public String trade_del(@RequestParam("tradingid") Long tradingId,  @AuthenticationPrincipal PrincipalDetails principalDetails,Model model){
        log.info("GET /user/myinfo/trading/del");
        String seller =  principalDetails.getUserDto().getUsername();
        boolean isDel =  tradingImageService.removeTradingImage(tradingId);

        if(!isDel){
            model.addAttribute("message","삭제 실패..");
            return "user/myinfo/trading/main";
        }
        model.addAttribute("message","삭제성공");
        return "user/myinfo/trading/main";
    }
    //----------------------------------------------------------------
    // 경매 취소 - 삭제 음악
    //----------------------------------------------------------------
    @GetMapping("/myinfo/trading/music/del")
    public String trade_del_music(@RequestParam("tradingid") Long tradingId,  @AuthenticationPrincipal PrincipalDetails principalDetails,Model model){
        log.info("GET /user/myinfo/trading/music/del");
        String seller =  principalDetails.getUserDto().getUsername();
        boolean isDel =  tradingMusicService.removeTradingMusic(tradingId);

        if(!isDel){
            model.addAttribute("message","삭제 실패..");
            return "user/myinfo/trading/main";
        }
        model.addAttribute("message","삭제성공");
        return "user/myinfo/trading/main";
    }


    @GetMapping(value="/myinfo/trading/download",produces=MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> downloadFile(@RequestParam("filepath") String filepath) throws UnsupportedEncodingException {
        log.info("GET /user/myinfo/trading/download.." + filepath);
        //FileSystemResource : 파일시스템의 특정 파일로부터 정보를 가져오는데 사용
        Resource resource = new FileSystemResource(filepath);
        //파일명 추출
        String filename = resource.getFilename();
        //헤더 정보 추가
        HttpHeaders headers = new HttpHeaders();
        //ISO-8859-1 : 라틴어(특수문자등 깨짐 방지)
        headers.add("Content-Disposition","attachment; filename="+new String(filename.getBytes("UTF-8"),"ISO-8859-1"));
        //리소스,파일정보가 포함된 헤더,상태정보를 전달
        return new ResponseEntity<Resource>(resource,headers,HttpStatus.OK);
    }
    //----------------------------------------------------------------
    // 송금 정보 입력
    //----------------------------------------------------------------
//    @PostMapping("/myinfo/receiveInfo")
//    public void receiveInfo(ReceiveInfo receiveInfo){
//
//
//    }
}
