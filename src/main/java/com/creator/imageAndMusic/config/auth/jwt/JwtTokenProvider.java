package com.creator.imageAndMusic.config.auth.jwt;



import com.creator.imageAndMusic.config.auth.PrincipalDetails;

import com.creator.imageAndMusic.domain.dto.UserDto;
import com.creator.imageAndMusic.properties.DBCONN;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {


    //Key 저장
    private final Key key;

    String url  = DBCONN.URL;
    String username = DBCONN.ID;
    String password  = DBCONN.PW;
    Connection conn;
    PreparedStatement pstmt;
    ResultSet rs;

        public JwtTokenProvider() throws Exception {

            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url,username,password);
            pstmt = conn.prepareStatement("select * from signature");
            rs =pstmt.executeQuery();

            if(rs.next())
            {

                byte [] keyByte =  rs.getBytes("signature");                    //DB로 서명Key꺼내옴
                this.key = Keys.hmacShaKeyFor(keyByte);                                    //this.key에 저장
//                .out.println("[JwtTokenProvider] Key : " + this.key );
            }
            else {
                byte[] keyBytes = KeyGenerator.getKeygen();     //난수키값 가져오기
                this.key = Keys.hmacShaKeyFor(keyBytes);        // 생성된 키를 사용하여 HMAC SHA(암호화알고리즘)알고리즘에 기반한 Key 객체 생성
                pstmt = conn.prepareStatement("insert into signature values(?,now())");

                pstmt.setBytes(1, keyBytes);
                pstmt.executeUpdate();
//                .out.println("[JwtTokenProvider] Constructor Key init: " + key);
            }


        }

    // 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
    public TokenInfo generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        UserDto userDto = principalDetails.getUserDto();
        //System.out.println("JWT GENERATE TOKEN DTO : " + userDto);
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + 60*30*1000);    // 60*30 초후 만료
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("username",authentication.getName())             //정보저장
                .claim("phone",userDto.getPhone())                      //정보저장
                .claim("nickname",userDto.getNickname())                //정보저장
                .claim("addr1",userDto.getAddr1())                      //정보저장
                .claim("addr2",userDto.getAddr2())                      //정보저장
                .claim("password",userDto.getPassword())                //정보저장
                .claim("zipcode",userDto.getZipcode())                  //정보저장
                .claim("bankname",userDto.getBankname())                  //정보저장
                .claim("account",userDto.getAccount())                  //정보저장

                .claim("auth", authorities)                             //정보저장
                .claim("principal", authentication.getPrincipal())      //정보저장
                .claim("credentials", authentication.getCredentials())  //정보저장
                .claim("details", authentication.getDetails())          //정보저장
                .claim("provider", userDto.getProvider())               //정보저장
                .claim("providerId",userDto.getProviderId())             //정보저장
                .claim("profileImage",userDto.getProfileImage())             //정보저장

                .claim("accessToken", principalDetails.getAccessToken())//정보저장
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + 86400000))    //1일: 24 * 60 * 60 * 1000 = 86400000
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

//        .out.println("[JwtTokenProvider] generateToken() accessToken : " + accessToken);
//        .out.println("[JwtTokenProvider] generateToken() refreshToken : " + refreshToken);

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenInfo generateToken(String Claimkey,String id,boolean isAuth) {

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + 60*5*1000);    // 60*5 초후 만료
        String accessToken = Jwts.builder()
                .setSubject(Claimkey+"JWT TOKEN")
                .claim(Claimkey,isAuth)             //정보저장
                .claim("id",id)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + 86400000))    //1일: 24 * 60 * 60 * 1000 = 86400000
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

//        .out.println("[JwtTokenProvider] generateToken() accessToken : " + accessToken);
//        .out.println("[JwtTokenProvider] generateToken() refreshToken : " + refreshToken);

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }



    public TokenInfo generateToken(String Claimkey,String id,boolean isAuth,String username) {

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + 60*5*1000);    // 60*5 초후 만료
        String accessToken = Jwts.builder()
                .setSubject(Claimkey+"JWT TOKEN")
                .claim(Claimkey,isAuth)             //정보저장
                .claim("id",id)
                .claim("username",username)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + 86400000))    //1일: 24 * 60 * 60 * 1000 = 86400000
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

//        .out.println("[JwtTokenProvider] generateToken() accessToken : " + accessToken);
//        .out.println("[JwtTokenProvider] generateToken() refreshToken : " + refreshToken);

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }




    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
            // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(auth -> new SimpleGrantedAuthority(auth))
                        .collect(Collectors.toList());

        //String username = claims.getSubject(); //username
        String username =  (String)claims.get("username");
        String phone =  (String)claims.get("phone");
        String nickname = (String)claims.get("nickname");
        String addr1 = (String)claims.get("addr1");
        String addr2 = (String)claims.get("addr2");
        String password = (String)claims.get("password");
        String zipcode = (String)claims.get("zipcode");
        String bankname = (String)claims.get("bankname");
        String account = (String)claims.get("account");



        String auth = (String)claims.get("auth");
        LinkedHashMap principal = (LinkedHashMap)claims.get("principal");
        String credentials = (String)claims.get("credentials");
        LinkedHashMap details = (LinkedHashMap)claims.get("details");
        String provider = (String)claims.get("provider");
        String providerId = (String)claims.get("providerId");
        String oauthAccessToken = (String)claims.get("accessToken");
        String profileImage = (String)claims.get("profileImage");





        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);
        userDto.setProvider(provider);
        userDto.setProviderId(providerId);
        userDto.setPhone(phone);
        userDto.setAddr1(addr1);
        userDto.setAddr2(addr2);
        userDto.setZipcode(zipcode);
        userDto.setNickname(nickname);
        userDto.setRole(auth);
        userDto.setBankname(bankname);
        userDto.setAccount(account);
        userDto.setProfileImage(profileImage);
        PrincipalDetails principalDetails = new PrincipalDetails();
        principalDetails.setUserDto(userDto);
        principalDetails.setAccessToken(oauthAccessToken);   //Oauth AccessToken
//        .out.println("[JWTTOKENPROVIDER] getAuthentication() principalDetails  : " + principalDetails);

        //System.out.println("JWT GETAUTHENTICATION DTO : " + userDto);

        //JWT + NO REMEMBERME
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(principalDetails, claims.get("credentials"), authorities);
        return usernamePasswordAuthenticationToken;

    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        }
        catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            return false;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        } catch(Exception etc){
            log.info("기타예외");
            return false;
        }
        return false;
    }
}