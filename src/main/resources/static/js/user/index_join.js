////  ----------------------------------
////  페이지 시작 이벤트
////  ----------------------------------
document.addEventListener("DOMContentLoaded", function() {
        // 페이지 렌더링 후 실행할 코드 작성
        // 예: DOM 요소에 접근하거나 초기화 작업 수행
        console.log("페이지가 로드되었습니다!");

        const searchCookieName = "email_auth_";
        let isAuth = false;
        let email_auth_cookie = null;

        var decodedCookie = decodeURIComponent(document.cookie);
        // 쿠키 이름을 검색할 문자열 생성
        var cookieString = searchCookieName + "=";
        // 쿠키를 ; 로 분할하여 배열로 만듭니다.
        var cookieArray = decodedCookie.split(';');
        // 모든 쿠키에 대해 반복
        for(var i = 0; i < cookieArray.length; i++) {
            var cookie = cookieArray[i];
            // 공백 제거
            while (cookie.charAt(0) == ' ') {
                cookie = cookie.substring(1);
            }
            // 현재 쿠키가 원하는 쿠키 이름으로 시작하는지 확인
            if (cookie.indexOf(cookieString) == 0) {
                // 해당 쿠키가 존재한다면 true 반환
               isAuth =  true;
               email_auth_cookie = cookie;

            }
        }

        if(isAuth&&email_auth_cookie != null) //이메일 인증코드쿠키가 있다면
        {
                //email_auth_cookie 가 null이 아니라면 유효한지 요청 (axios)
                axios
                .get('/user/emailConfirm?emailCode=0')
                .then(res =>{
                              console.log(res);
                              if(res.data.success){
                                      //인증완료 -> 다음 입력을 할 준비~
                                      document.querySelector('.username_msg').style.color='green';
                                      document.joinform.username.value = res.data.username;
                                      document.joinform.username.readonly = true;
                                      document.querySelector('.username_msg').innerHTML=res.data.message;

                              }else{
                                      alert('기존 인증쿠키값을 제거합니다.');
                                      //해당 쿠키 제거
                                      document.cookie = email_auth_cookie + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
                                      location.reload();
                              }

                  })
                 .catch(err=>{console.log(err);});
        }else{
                //해당쿠키가 유효한지 서버로 체크
                console.log("isAuth",isAuth);
                const email_auth_btn = document.querySelector('.email_auth_btn');
                email_auth_btn.click();
        }





});



//---------------------------------------------
//이메일 인증버튼 클릭시
//---------------------------------------------
const email_auth_btn = document.querySelector(".email_auth_btn");
email_auth_btn.addEventListener("click",function(){
    console.log("email_auth_btn...");

    //이메일 유효성 체크
    const username = document.joinform.username.value;
    const username_msg = document.querySelector('.username_msg');
    var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if(username==null||username==''){
        username_msg.style.color="orange";
        username_msg.innerHTML="[*] 이메일을 반드시 입력하세요.";
    }
    else if(!emailRegex.test(username)){
        username_msg.style.color="orange";
        username_msg.innerHTML="[*] example@example.com";
    }
    else
    {

      //스피너 동작
      const join_block_bg = document.querySelector('.join_block_bg');
      join_block_bg.style.display='flex';

      username_msg.innerHTML="";

        //서버에 요청
        axios.get('/user/sendMail/' + username)
            .then(resp=>{
                    console.log(resp);
                    //인증 성공시에
                    const email_auth_block = document.querySelector(".email_auth_block");
                    email_auth_block.style.display = "flex";

                    join_block_bg.style.display='none';
            })
            .catch(err=>{console.log(err);});



    }

})

//---------------------------------------------
//인증코드버튼 클릭시 처리
//---------------------------------------------
const emailCodeForm_btn = document.querySelector('.emailCodeForm_btn');
        emailCodeForm_btn.addEventListener('click', function(){


                const join_block_bg = document.querySelector('.join_block_bg');
                join_block_bg.style.display='flex';


                const username = document.joinform.username.value;
                const emailCode = document.querySelector('.email_code').value;

                console.log('emailCodeForm_btn clicked..');
                axios
                .get('/user/emailConfirm?emailCode='+emailCode+'&username='+username)
                .then(res =>{
                              console.log(res);
                              if(res.data.success){
                                      //인증완료 -> 다음 입력을 할 준비~
                                      document.querySelector('.username_msg').style.color='green';
                                      document.joinform.username.value = username;
                                      document.joinform.username.readonly = true;
                                      document.querySelector('.username_msg').innerHTML=res.data.message;

                                        const email_auth_block = document.querySelector(".email_auth_block");
                                        email_auth_block.style.display = "none";

                                     join_block_bg.style.display='none';
                              }else{
                                      //인증X -> 인증이 안된 내용을 표시
                                      alert('이메일 인증 실패!');
                                      location.href="/";
                              }



                  })
                 .catch(err=>{console.log(err);});

        });


//---------------------------------------------
//회원가입 버튼 입력시
//---------------------------------------------
 const join_btn =  document.querySelector('.join_btn');
const joinform = document.joinform;
join_btn.addEventListener("click",function(){

        //유효성 체크(요휴하다면 요청처리
        if(isValid(joinform)){

            console.log('Validation Check Success..!')
            //유효성 체크 완료

            document.joinform.submit();

        }
        else
        {
            alert("Validation Check Error");
        }
});

//

//----------------------------------------------------------
//PASSWORD 이벤트 처리 유효성 검사
//----------------------------------------------------------
const password= joinform.password;
const password_msg = document.querySelector('.password_msg');

password.addEventListener("focus",function(){
    console.log("focuse..");
    if(password.value==null|| password.value==""){
        password_msg.style.color="orange";
        password_msg.style.fontSize=".7rem";
        password_msg.innerHTML ="[*] 패스워드 입력은 필수 사항입니다.";
    }
})
password.addEventListener("blur",function(){
    console.log("blur..");
    if(password.value==null|| password.value==""){
        password_msg.style.color="orange";
        password_msg.style.fontSize=".7rem";
        password_msg.innerHTML ="[*] 패스워드 입력은 필수 사항입니다.";
    }
})

password.addEventListener('input',function(e){
        console.log(e.target.value);
        //영문대/소/특수문자/숫자 중 3개이상 포함여부
        var regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,16}$/;
        if(password.value==null||password.value==""){
                password_msg.style.color="orange";
                password_msg.style.fontSize=".7rem";
                password_msg.innerHTML ="[*] 패스워드는 필수 입력 사항 입니다.";
        }
        if(password.value.length<8 || password.value.lenght>16){
               password_msg.style.color="orange";
               password_msg.style.fontSize=".7rem";
                password_msg.innerHTML ="[*] 패스워드 길이는 8 - 16 로 입력합니다.";
        }
        else if(!regex.test(password.value)){
            password_msg.style.color="orange";
            password_msg.style.fontSize=".7rem";
            password_msg.innerHTML ="[*] 대/소/특수/숫자 각각 1개이상 포함해야합니다.";
        }
        else
        {
            password_msg.style.color="green";
            password_msg.style.fontSize=".7rem";
            password_msg.innerHTML ="패스워드를 올바르게 입력했습니다.";
        }
});


//----------------------------------------------------------
//REPASSWORD 이벤트 처리 유효성 검사
//----------------------------------------------------------
const repassword= joinform.repassword;
const repassword_msg = document.querySelector('.repassword_msg');

repassword.addEventListener("focus",function(){
    console.log("focuse..");
    if(password.value==null|| password.value==""){
        repassword_msg.style.color="orange";
        repassword_msg.style.fontSize=".7rem";
        repassword_msg.innerHTML ="[*] 패스워드 확인입력은 필수 사항입니다.";
    }
    else if(password.value!==repassword.value){
                repassword_msg.style.color="orange";
                repassword_msg.style.fontSize=".7rem";
                repassword_msg.innerHTML ="[*] 패스워드가 일치하지 않습니다.";
    }else{
                 repassword_msg.style.color="green";
                 repassword_msg.style.fontSize=".7rem";
                 repassword_msg.innerHTML ="패스워드 확인을 완료했습니다.";

    }
})
repassword.addEventListener("blur",function(){
    console.log("blur..");
    if(password.value==null|| password.value==""){
        repassword_msg.style.color="orange";
        repassword_msg.style.fontSize=".7rem";
        repassword_msg.innerHTML ="[*] 패스워드 확인입력은 필수 사항입니다.";
    }
    else if(password.value!==repassword.value){
                repassword_msg.style.color="orange";
                repassword_msg.style.fontSize=".7rem";
                repassword_msg.innerHTML ="[*] 패스워드가 일치하지 않습니다.";
    }else{
            repassword_msg.style.color="green";
            repassword_msg.style.fontSize=".7rem";
            repassword_msg.innerHTML ="패스워드 확인을 완료했습니다.";

    }
})

repassword.addEventListener('input',function(e){
        console.log(e.target.value);

        if(repassword.value==null||repassword.value==""){
                repassword_msg.style.color="orange";
                repassword_msg.style.fontSize=".7rem";
                repassword_msg.innerHTML ="[*] 패스워드는 필수 입력 사항 입니다.";
        }
        if(repassword.value.length<8 || repassword.value.lenght>16){
               repassword_msg.style.color="orange";
               repassword_msg.style.fontSize=".7rem";
               repassword_msg.innerHTML ="[*] 패스워드 길이는 8 - 16 로 입력합니다.";
        }
        else if(password.value!==repassword.value){
            repassword_msg.style.color="orange";
            repassword_msg.style.fontSize=".7rem";
            repassword_msg.innerHTML ="[*] 패스워드가 일치하지 않습니다.";
        }
        else
        {
            repassword_msg.style.color="green";
            repassword_msg.style.fontSize=".7rem";
            repassword_msg.innerHTML ="패스워드 확인을 완료했습니다.";
        }
});

//----------------------------------------------------------
//NAME 이벤트 처리 유효성 검사
//----------------------------------------------------------
const nickname= joinform.nickname;
const nickname_msg = document.querySelector('.nickname_msg');
nickname.addEventListener("focus",function(){
    console.log("focuse..");
    if(nickname.value==null|| nickname.value==""){
        nickname_msg.style.color="orange";
        nickname_msg.style.fontSize=".7rem";
        nickname_msg.innerHTML ="[*] 이름입력은 필수 사항입니다.";
    }else{
                 nickname_msg.style.color="green";
                 nickname_msg.style.fontSize=".7rem";
                 nickname_msg.innerHTML ="이름입력을  완료했습니다.";
    }

    //패스워드확인 체크
            if(repassword.value==null||repassword.value==""){
                    repassword_msg.style.color="orange";
                    repassword_msg.style.fontSize=".7rem";
                    repassword_msg.innerHTML ="[*] 패스워드는 필수 입력 사항 입니다.";
            }
            if(repassword.value.length<8 || repassword.value.lenght>16){
                   repassword_msg.style.color="orange";
                   repassword_msg.style.fontSize=".7rem";
                   repassword_msg.innerHTML ="[*] 패스워드 길이는 8 - 16 로 입력합니다.";
            }
            else if(password.value!==repassword.value){
                repassword_msg.style.color="orange";
                repassword_msg.style.fontSize=".7rem";
                repassword_msg.innerHTML ="[*] 패스워드가 일치하지 않습니다.";
            }
            else
            {
                repassword_msg.style.color="green";
                repassword_msg.style.fontSize=".7rem";
                repassword_msg.innerHTML ="패스워드 확인을 완료했습니다.";
            }

})
nickname.addEventListener("blur",function(){
    console.log("blur..");
    if(nickname.value==null|| nickname.value==""){
            nickname_msg.style.color="orange";
            nickname_msg.style.fontSize=".7rem";
            nickname_msg.innerHTML ="[*] 이름입력은 필수 사항입니다.";
    }
    else
    {
             nickname_msg.style.color="green";
             nickname_msg.style.fontSize=".7rem";
             nickname_msg.innerHTML ="이름입력을  완료했습니다.";
    }

    //
            if(repassword.value==null||repassword.value==""){
                    repassword_msg.style.color="orange";
                    repassword_msg.style.fontSize=".7rem";
                    repassword_msg.innerHTML ="[*] 패스워드는 필수 입력 사항 입니다.";
            }
            if(repassword.value.length<8 || repassword.value.lenght>16){
                   repassword_msg.style.color="orange";
                   repassword_msg.style.fontSize=".7rem";
                   repassword_msg.innerHTML ="[*] 패스워드 길이는 8 - 16 로 입력합니다.";
            }
            else if(password.value!==repassword.value){
                repassword_msg.style.color="orange";
                repassword_msg.style.fontSize=".7rem";
                repassword_msg.innerHTML ="[*] 패스워드가 일치하지 않습니다.";
            }
            else
            {
                repassword_msg.style.color="green";
                repassword_msg.style.fontSize=".7rem";
                repassword_msg.innerHTML ="패스워드 확인을 완료했습니다.";
            }
})
nickname.addEventListener('input',function(e){
        console.log(e.target.value);
        if(nickname.value==null|| nickname.value==""){
                    nickname_msg.style.color="orange";
                    nickname_msg.style.fontSize=".7rem";
                    nickname_msg.innerHTML ="[*] 이름입력은 필수 사항입니다.";
        }else{
            nickname_msg.style.color="green";
            nickname_msg.style.fontSize=".7rem";
            nickname_msg.innerHTML ="이름입력을  완료했습니다.";

        }

        if(repassword.value==null||repassword.value==""){
                        repassword_msg.style.color="orange";
                        repassword_msg.style.fontSize=".7rem";
                        repassword_msg.innerHTML ="[*] 패스워드는 필수 입력 사항 입니다.";
       }
       if(repassword.value.length<8 || repassword.value.lenght>16){
                       repassword_msg.style.color="orange";
                       repassword_msg.style.fontSize=".7rem";
                       repassword_msg.innerHTML ="[*] 패스워드 길이는 8 - 16 로 입력합니다.";
       }
       else if(password.value!==repassword.value){
                    repassword_msg.style.color="orange";
                    repassword_msg.style.fontSize=".7rem";
                    repassword_msg.innerHTML ="[*] 패스워드가 일치하지 않습니다.";
      }
      else
      {
                    repassword_msg.style.color="green";
                    repassword_msg.style.fontSize=".7rem";
                    repassword_msg.innerHTML ="패스워드 확인을 완료했습니다.";
      }

});


//----------------------------------------------------------
//PHONE 이벤트 처리 유효성 검사
//----------------------------------------------------------
const phone= joinform.phone;
const phone_msg = document.querySelector('.phone_msg');
phone.addEventListener("focus",function(){
    console.log("focuse..");
    if(phone.value==null|| phone.value==""){
        phone_msg.style.color="orange";
        phone_msg.style.fontSize=".7rem";
        phone_msg.innerHTML ="[*] 전화번호 입력은 필수 사항입니다.";
    }

            if(repassword.value==null||repassword.value==""){
                    repassword_msg.style.color="orange";
                    repassword_msg.style.fontSize=".7rem";
                    repassword_msg.innerHTML ="[*] 패스워드는 필수 입력 사항 입니다.";
            }
            if(repassword.value.length<8 || repassword.value.lenght>16){
                   repassword_msg.style.color="orange";
                   repassword_msg.style.fontSize=".7rem";
                   repassword_msg.innerHTML ="[*] 패스워드 길이는 8 - 16 로 입력합니다.";
            }
            else if(password.value!==repassword.value){
                repassword_msg.style.color="orange";
                repassword_msg.style.fontSize=".7rem";
                repassword_msg.innerHTML ="[*] 패스워드가 일치하지 않습니다.";
            }
            else
            {
                repassword_msg.style.color="green";
                repassword_msg.style.fontSize=".7rem";
                repassword_msg.innerHTML ="패스워드 확인을 완료했습니다.";
            }
})
phone.addEventListener("blur",function(){
    console.log("blur..");
    if(phone.value==null|| phone.value==""){
       phone_msg.style.color="orange";
       phone_msg.style.fontSize=".7rem";
       phone_msg.innerHTML ="[*] 전화번호 입력은 필수 사항입니다.";
    }

            if(repassword.value==null||repassword.value==""){
                    repassword_msg.style.color="orange";
                    repassword_msg.style.fontSize=".7rem";
                    repassword_msg.innerHTML ="[*] 패스워드는 필수 입력 사항 입니다.";
            }
            if(repassword.value.length<8 || repassword.value.lenght>16){
                   repassword_msg.style.color="orange";
                   repassword_msg.style.fontSize=".7rem";
                   repassword_msg.innerHTML ="[*] 패스워드 길이는 8 - 16 로 입력합니다.";
            }
            else if(password.value!==repassword.value){
                repassword_msg.style.color="orange";
                repassword_msg.style.fontSize=".7rem";
                repassword_msg.innerHTML ="[*] 패스워드가 일치하지 않습니다.";
            }
            else
            {
                repassword_msg.style.color="green";
                repassword_msg.style.fontSize=".7rem";
                repassword_msg.innerHTML ="패스워드 확인을 완료했습니다.";
            }


})
phone.addEventListener('input',function(e){
        console.log(e.target.value);
        event.target.value = event.target.value.replace(/\D/g, "");

        var regex = /\d+/;
        if(!regex.test(event.target.value)){
            phone_msg.innerHTML ="[*] 숫자를 입력해야 합니다";
        }
        else if ( event.target.value.length > 0 &&  event.target.value.charAt(0) !== '0') {
           // 첫 문자가 0이 아니라면, 입력된 내용에서 숫자를 제거합니다.
            phone_msg.innerHTML ="[*] 첫번째 숫자는 0 이어야 합니다";
            event.target.value = event.target.value.replace(/\D/g, "");
         }
        else if(event.target.value.length>12){
            phone_msg.innerHTML ="[*] 전화번호의 길이가 너무 깁니다.";
        }
         else {
                 // 첫 문자가 0이라면, 숫자만 남기도록 처리합니다.
            event.target.value = event.target.value.replace(/\D+/g, "");
            phone_msg.innerHTML ="";
        }

        if(repassword.value==null||repassword.value==""){
                repassword_msg.style.color="orange";
                repassword_msg.style.fontSize=".7rem";
                repassword_msg.innerHTML ="[*] 패스워드는 필수 입력 사항 입니다.";
        }
        if(repassword.value.length<8 || repassword.value.lenght>16){
               repassword_msg.style.color="orange";
               repassword_msg.style.fontSize=".7rem";
               repassword_msg.innerHTML ="[*] 패스워드 길이는 8 - 16 로 입력합니다.";
        }
        else if(password.value!==repassword.value){
            repassword_msg.style.color="orange";
            repassword_msg.style.fontSize=".7rem";
            repassword_msg.innerHTML ="[*] 패스워드가 일치하지 않습니다.";
        }
        else
        {
            repassword_msg.style.color="green";
            repassword_msg.style.fontSize=".7rem";
            repassword_msg.innerHTML ="패스워드 확인을 완료했습니다.";
        }

});



    //---------------------------------------------
    // Data Valication Check Function
    //---------------------------------------------
    function isValid(form){

            const username_msg = document.querySelector('.username_msg');
            const password_msg = document.querySelector('.password_msg');
            const repassword_msg = document.querySelector('.repassword_msg');
            const nickname_msg = document.querySelector('.nickname_msg');
            const phone_msg = document.querySelector('.phone_msg');

            const password= form.password;
            const repassword= form.repassword;
            const nickname= form.nickname;
            const phone = form.phone;

            //---------------------------------------------
            // 패스워드 유효성 검증
            //---------------------------------------------
            var regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,16}$/;
            if(password.value==null||password.value==""){
                    password_msg.style.color="orange";
                    password_msg.style.fontSize=".7rem";
                    password_msg.innerHTML ="[*] 패스워드는 필수 입력 사항 입니다.";
                    return false;
            }
            if(password.value.length<8 || password.value.lenght>16){
                   password_msg.style.color="orange";
                   password_msg.style.fontSize=".7rem";
                    password_msg.innerHTML ="[*] 패스워드 길이는 8 - 16 로 입력합니다.";
                    return false;
            }
            else if(!regex.test(password.value)){
                password_msg.style.color="orange";
                password_msg.style.fontSize=".7rem";
                password_msg.innerHTML ="[*] 대/소/특수/숫자 각각 1개이상 포함해야합니다.";
                return false;
            }
            else
            {
                password_msg.style.color="green";
                password_msg.style.fontSize=".7rem";
                password_msg.innerHTML ="패스워드를 올바르게 입력했습니다.";
            }

            //---------------------------------------------
            // 패스워드 확인 유효성 검증
            //---------------------------------------------

            if(repassword.value==null||repassword.value==""){
                    repassword_msg.style.color="orange";
                    repassword_msg.style.fontSize=".7rem";
                    repassword_msg.innerHTML ="[*] 패스워드는 필수 입력 사항 입니다.";
                    return false;
            }
            if(repassword.value.length<8 || repassword.value.lenght>16){
                   repassword_msg.style.color="orange";
                   repassword_msg.style.fontSize=".7rem";
                   repassword_msg.innerHTML ="[*] 패스워드 길이는 8 - 16 로 입력합니다.";
                   return false;
            }
            else if(password.value!==repassword.value){
                repassword_msg.style.color="orange";
                repassword_msg.style.fontSize=".7rem";
                repassword_msg.innerHTML ="[*] 패스워드가 일치하지 않습니다.";
                return false;
            }
            else
            {
                repassword_msg.style.color="green";
                repassword_msg.style.fontSize=".7rem";
                repassword_msg.innerHTML ="패스워드 확인을 완료했습니다.";
            }

            //---------------------------------------------
            // 이름 입력 여부 확인
            //---------------------------------------------
            if(nickname.value==null|| nickname.value==""){
                        nickname_msg.style.color="orange";
                        nickname_msg.style.fontSize=".7rem";
                        nickname_msg.innerHTML ="[*] 이름입력은 필수 사항입니다.";
                        return false;
            }else{
                nickname_msg.style.color="green";
                nickname_msg.style.fontSize=".7rem";
                nickname_msg.innerHTML ="이름입력을  완료했습니다.";

            }
            //---------------------------------------------
            // 연락처 입력 여부 확인
            //---------------------------------------------
            if(phone.value==null|| phone.value==""){
               phone_msg.style.color="orange";
               phone_msg.style.fontSize=".7rem";
               phone_msg.innerHTML ="[*] 전화번호 입력은 필수 사항입니다.";
               return false;
            }

           return true;
        }

    //---------------------------------------------
    //---------------------------------------------
    //---------------------------------------------
    //---------------------------------------------






