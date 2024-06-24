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


//  ----------------------------------
//  주소창 띄우기
//  ----------------------------------
const search_addr_btn = document.querySelector('.search_addr_btn');
search_addr_btn.addEventListener('click',function(){
    new daum.Postcode({
        oncomplete: function(data) {
            console.log(data);
            const zipcode = document.querySelector('.zipcode');
            const addr1 = document.querySelector('.addr1');

            if(data.userSelectedType==='R')
            {
                addr1.value = data.roadAddress;
            }
            else
            {
                addr1.value = data.jibunAddress;
            }
            zipcode.value=data.zonecode;

        }
    }).open();

});
//  ----------------------------------
//  account_confirm
//  ----------------------------------

const account_confirm = document.querySelector('.account_confirm');

account_confirm.addEventListener('click',function(){
    const bankCode = document.mypageform.bankname.value;
    const account = document.mypageform.account.value;
    const formData = new FormData();
    formData.append("bankCode",bankCode);
    formData.append("account",account);

        axios.post('/user/comfirm/account',formData,{ headers: {'Content-Type' :'x-www.form-urlencoded' } } )
                .then(res=>{
                    console.log(res);

                    //location.href="/user/myinfo/read";
        })
                .catch(err=>{console.log(err);})

})