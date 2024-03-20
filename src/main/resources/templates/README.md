# RANKINGSYSTEM_FN
[조은파 Readme](./[조원]-조은파.md) | [강지영 Readme](./[조원]-강지영.md)
### RULE
---
- GITHUB COMMIT MESSAGE
  - git commit -m "[VERSION] [NAME] [DESCRIPTION]"
    - EX ] git commit -m "V1.2.3 JUNGWOOGYUN ADDED STYLE" 
      - 1 : Major Version(ex.페이지 추가/구조/기본스타일링) 
      - 2 : Miner Version(ex.기능추가)
      - 3 : Patch(ex.기존기능/스타일 수정

### HISTORY
---
|VERSION|DATE|CATEGORY|DESCRIPTION|
|------|---|---|---|
|V0.0.0|2024-03-19|INIT|기본 HTML/CSS/JS 파일생성/연결/코드구현|
|V0.0.1|테스트1|테스트2|테스트3|
|V0.0.2|테스트1|테스트2|테스트3|



### PLAN
---
|DATE|CATEGORY|PATH|CONTENT|LEVEL|ISSUCCEED|
|----------|--|----|-----|---|---|
|2024-04-01|FE|/css/common.css|main태그영역 기본레이아웃|OPTIONAL|FALSE|
|2024-04-01|FE|/css/common.css|기본 해상도 16:9로 가로세로 크기 설정|OPTIONAL|FALSE|
|2024-04-01|FE|/css/mobile/*.css|각페이지별 모바일 적용 코드 구현|**IMPORTANT**|FALSE|
|2024-04-01|FE|/css/user/album/main.css|표시 이미지(.item)블럭단위 기본크기 설정|**IMPORTANT**|FALSE|
|2024-04-01|FE|/css/user/album/main.css|앨범페이지 필터 서브창띄우고 선택(조회순/좋아요 순)|**IMPORTANT**|FALSE|
|2024-04-01|FE|/css/common/css|로고 선정 후 수정|OPTIONAL|FALSE|
|-|-|-|-|-|-|
|-|-|-|-|-|-|
|2024-04-01|BE|/user/join|회원가입|**IMPORTANT**| FASLE
|2024-04-01|BE|/login,/logout|로그인/로그아웃|**IMPORTANT**| FASLE
|2024-04-01|BE|/login|REMEMBER_ME|OPTIONAL| FASLE
|2024-04-01|BE|/user/reid,/user/repassword|아이디/패스워드 복구|OPTIONAL| FASLE
|2024-04-01|BE|/user/album/upload|이미지 파일 업로드|**IMPORTANT**| FASLE
|2024-04-01|BE|/user/album/main|이미지 전체 조회|**IMPORTANT**| FASLE
|2024-04-01|BE|/user/album/main?keyfield=?&keyword=?|이미지 키워드(좋아요/조회순) 조회|**IMPORTANT**| FASLE
|2024-04-01|BE|미정|이미지 랭킹 조회|**IMPORTANT**| FASLE

### TEAM
---
|NAME|ROLE|GITHUBLINK|
|------|---|---|
|홍길동|조장-BACKEND/CONTROLLER | github://~~~
|강지영|조원-FRONTEND|테스트3|
|조은파|조원-FRONTEND|테스트3|


### USE SKILLS
---

CSS LINK
  -
  -
  -
  
JS CDN
  -
  -
  -

SPRING BOOT DEPENDENCIES
  -
  -
  -
  




### CDN LINK
---
-
-
-


### FILE TREE
---

#### HTML
----
- template.html
- index.html
- login.html
- /user
  - myinfo.html
  - /album
    - add.html
    - main.html

#### CSS
---
- /css
  - common.css
  - login.css
  - /mobile
    - /user
      - /album
        - add.css
        - main.css
      - /bookmark
        - main.css  
  - /user
    - /album
      - add.css
      - main.css
    - /bookmark
      - main.css 

#### JS
---
- /js
  - common.js
  - /user
    - /album
      - main.js
    - /bookmark
      - main.js

  

