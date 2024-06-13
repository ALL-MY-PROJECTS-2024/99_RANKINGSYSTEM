
const delete_btn = document.querySelector('.delete_btn');
delete_btn.addEventListener('click',function(){

    const form = document.deleteForm;
    axios.delete("/user/myinfo/delete?password="+form.password.value)
    .then(resp=>{
        console.log(resp);
        alert("계정 삭제 완료! 메인페이지로 이동합니다.")
        location.href="/";
    })
    .catch(err=>console.log(err))
})