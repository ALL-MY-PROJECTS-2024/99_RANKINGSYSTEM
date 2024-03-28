
//https://sumni.tistory.com/152
const confirmId_btn = document.querySelector('.confirmId_btn');
confirmId_btn.addEventListener('click',function(){
    const check_id_form = document.checkIdform;
    console.log(check_id_form);
    const nickname = check_id_form.nickname.value;
    const phone = check_id_form.phone.value;
    const formData = new FormData();
    formData.append('nickname', nickname);
    formData.append('phone', phone);

    axios.post('/user/confirmId', formData ,  {headers: {'Content-Type': 'application/json'}})
    .then(resp=>{
        console.log(resp)
        alert("USERNAME : " + resp.data);
        }
    )
    .catch(err=>{
        console.log(err);
        alert(err.response.data);
    })

    console.log("confirmId_btn clicked..");


})

