//---------------------------------------------
//회원가입 버튼 입력시
//---------------------------------------------
 const join_btn =  document.querySelector('.join_btn');
const joinform = document.joinform;
join_btn.addEventListener("click",function(){

        //유효성 체크(요휴하다면 요청처리
        if(isValid(joinform)){


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
    //---------------------------------------------
    function isValid(form){
    //
    //        const username_msg = document.querySelector('.username_msg');
    //        const password_msg = document.querySelector('.password_msg');
    //        const repassword_msg = document.querySelector('.repassword_msg');
    //        const nickname_msg = document.querySelector('.nickname_msg');
    //        const phone_msg = document.querySelector('.phone_msg');
    //
    //
    //        if(form.password==null||form.password==""){
    //
    //        }
    //
    //
    //        return true;
        }

    //---------------------------------------------
    //---------------------------------------------
    //---------------------------------------------
    //---------------------------------------------






