const confirmpasswordbtn = document.querySelector('.confirmpasswordbtn');

confirmpasswordbtn.addEventListener('click', function(){
    console.log('click');
    const oldpassword = document.mypageform.oldpassword.value;

    const formData = new FormData();
    formData.append('password', oldpassword);

    axios.post('/user/myinfo/confirmPw',formData,{headers: {'Content-Type': 'application/x-www-form-urlencoded',},})
    .then(resp=>{
        console.log(resp)
        document.querySelector('.pwmsg').style.color="green";
        document.querySelector('.pwmsg').innerHTML="패스워드 확인 완료";
        document.mypageform.password.readOnly=false;
        document.mypageform.repassword.readOnly=false;

     })
    .catch(err=>{
        console.log(err);
        document.querySelector('.pwmsg').style.color="red";
        document.querySelector('.pwmsg').innerHTML="패스워드를 다시 입력하세요";

        document.mypageform.password.readOnly=true;
        document.mypageform.repassword.readOnly=true;

    })
})