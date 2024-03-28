
//https://sumni.tistory.com/152
const confirmPw_btn = document.querySelector('.confirmPw_btn');
confirmPw_btn.addEventListener('click',function(){
    const check_pw_form = document.checkPwform;
    console.log(check_pw_form);
    const username = check_pw_form.username.value;
    const nickname = check_pw_form.nickname.value;
    const phone = check_pw_form.phone.value;
    const formData = new FormData();
    formData.append('username', username);
    formData.append('nickname', nickname);
    formData.append('phone', phone);

    axios.post('/user/confirmPw', formData ,  {headers: {'Content-Type': 'application/json'}})
    .then(resp=>{
        console.log(resp)
            alert(resp.data);
        }
    )
    .catch(err=>{
        console.log(err);
        alert(err.response.data);
    })

    console.log("confirmId_btn clicked..");


})

