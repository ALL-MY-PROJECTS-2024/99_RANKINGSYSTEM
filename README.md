# RANKINGSYSTEM_BN

### HISTORY
---
|VERSION|DATE|CATEGORY|DESCRIPTION|
|------|---|---|---|
|V0.0.0|2024-03-20|AUTH|로그인/로그아웃/회원가입/REMEMBERME 구현 완료 |
|V0.0.1|2024-03-20|ALBUM|/USER/ALBUM/ADD 구현 진행중 |

### TEAM
---
|NAME|ROLE|GITHUBLINK|
|------|---|---|
|홍길동|조장-BACKEND/CONTROLLER | -
|강지영|조원-FRONTEND|-|
|조은파|조원-FRONTEND|-|

### PLAN
---
|DATE|CATEGORY|PATH|METHOD|CONTENT|LEVEL|ISSUCCEED|
|-|-|-|-|-|-|-|
|2024-04-01|BE|/user/join|GET|회원가입|**IMPORTANT**| **TRUE**
|2024-04-01|BE|/user/join|POST|회원가입|**IMPORTANT**| **TRUE**
|2024-04-01|BE|/login<br>/logout|GET|로그인/로그아웃|**IMPORTANT**| **TRUE**
|2024-04-01|BE|/login<br>/logout|POST|로그인/로그아웃|**IMPORTANT**| **TRUE**
|2024-04-01|BE|-|-|REMEMBER_ME|OPTIONAL| **TRUE**
|2024-04-01|BE|/user/reid<br>/user/repassword|-|아이디/패스워드 복구|OPTIONAL| FASLE
|2024-04-01|BE|/user/album/upload|-|이미지 파일 업로드|**IMPORTANT**| FASLE
|2024-04-01|BE|-|-|이미지 전체 조회|**IMPORTANT**| FASLE
|2024-04-01|BE|/user/album/main?keyfield=?&keyword=?|-|이미지 키워드(좋아요/조회순) 조회|**IMPORTANT**| FASLE
|2024-04-01|BE|미정|-|이미지 랭킹 조회|**IMPORTANT**| FASLE



### USE SKILLS
---
 
<img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> <img src="https://img.shields.io/badge/CSS-1572B6?style=for-the-badge&logo=css3&logoColor=white"> <img src="https://img.shields.io/badge/JAVASCRIPT-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white"> <img src="https://img.shields.io/badge/JAVA-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white"> <img src="https://img.shields.io/badge/SPRINGBOOT-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white"> <img src="https://img.shields.io/badge/MYSQL-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white"> <img src="https://img.shields.io/badge/AWS-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white"> 

### DEPENDENCIES
---

#### CSS CDN
  - 01
  - 01
  - 01
  
#### JS CDN
  - 01
  - 01
  - 01

#### SPRING BOOT DEPENDENCIES
  - 01
  - 01
  - 01
  

### FILE TREE 
---

C:.
└─main
    ├─generated
    ├─java
    │  └─com
    │      └─creator
    │          └─imageAndMusic
    │              │  ImageAndMusicApplication.java
    │              │
    │              ├─config
    │              │  │  DataSourceConfig.java
    │              │  │  SecurityConfig.java
    │              │  │  TxConfig.java
    │              │  │  WebMvcConfig.java
    │              │  │
    │              │  └─auth
    │              │      │  PrincipalDetails.java
    │              │      │  PrincipalDetailsOAuth2Service.java
    │              │      │  PrincipalDetailsService.java
    │              │      │
    │              │      ├─exceptionHandler
    │              │      │      CustomAccessDeniedHandler.java
    │              │      │      CustomAuthenticationEntryPoint.java
    │              │      │
    │              │      ├─jwt
    │              │      │      JwtAuthenticationFilter.java
    │              │      │      JwtAuthorizationFilter.java
    │              │      │      JwtProperties.java
    │              │      │      JwtTokenProvider.java
    │              │      │      KeyGenerator.java
    │              │      │      TokenInfo.java
    │              │      │
    │              │      ├─loginHandler
    │              │      │      CustomAuthenticationFailureHandler.java
    │              │      │      CustomLoginSuccessHandler.java
    │              │      │      Oauth2JwtLoginSuccessHandler.java
    │              │      │
    │              │      ├─logoutHandler
    │              │      │      CustomLogoutHandler.java
    │              │      │      CustomLogoutSuccessHandler.java
    │              │      │
    │              │      └─provider
    │              │              GoogleUserInfo.java
    │              │              KakaoUserInfo.java
    │              │              NaverUserInfo.java
    │              │              OAuth2UserInfo.java
    │              │
    │              ├─controller
    │              │      HomeController.java
    │              │      UserController.java
    │              │
    │              ├─domain
    │              │  ├─dto
    │              │  │      ImageDto.java
    │              │  │      UserDto.java
    │              │  │
    │              │  ├─entity
    │              │  │      Images.java
    │              │  │      Signature.java
    │              │  │      User.java
    │              │  │
    │              │  ├─repository
    │              │  │      ImagesRepository.java
    │              │  │      UserRepository.java
    │              │  │
    │              │  └─service
    │              │          UserService.java
    │              │          UserServiceImpl.java
    │              │
    │              └─properties
    │                      UPLOADPATH.java
    │
    └─resources
        │  application.properties
        │  data.sql
        │
        ├─static
        │  ├─assets
        │  │      top-header-left.jpg
        │  │
        │  ├─css
        │  │  │  common.css
        │  │  │  login.css
        │  │  │
        │  │  ├─mobile
        │  │  │      album.css
        │  │  │      common.css
        │  │  │      login.css
        │  │  │
        │  │  └─user
        │  │      │  join.css
        │  │      │
        │  │      ├─album
        │  │      │      add.css
        │  │      │      main.css
        │  │      │
        │  │      └─bookmark
        │  │              main.css
        │  │
        │  └─js
        │      │  common.js
        │      │
        │      └─user
        │          ├─album
        │          │      add.js
        │          │      main.js
        │          │
        │          └─bookmark
        │                  main.js
        │
        └─templates
            │  index.html
            │  login.html
            │  README.md
            │  template.html
            │
            └─user
                │  join.html
                │  myinfo.html
                │
                └─album
                        add.html
                        main.html

                        



### RULE
---
- GITHUB COMMIT MESSAGE
  - git commit -m "[VERSION] [NAME] [DESCRIPTION]"
    - EX ] git commit -m "V1.2.3 JUNGWOOGYUN ADDED STYLE" 
      - 1 : Major Version(ex.페이지 추가/구조/기본스타일링) 
      - 2 : Miner Version(ex.기능추가)
      - 3 : Patch(ex.기존기능/스타일 수정

