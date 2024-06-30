const contentArea = document.querySelector('.content-area');
const replyAddBtn = document.querySelector('.reply_add_btn');

contentArea.addEventListener('keydown',function(e){
//    console.log('keydown..' + e.keyCode);
//    if(e.keyCode==13){
//        console.log('엔터..');
//    }
})
replyAddBtn.addEventListener('click',function(){
    console.log('clicked..');

    const bno = document.querySelector('.bno').value;
    const content = contentArea.value;

    axios.get(`/board/reply/add?bno=${bno}&content=${content}`)
    .then(resp=>{
        console.log(resp)
        alert("댓글 등록 완료");
        location.reload();
    })
    .catch(err=>{console.log(err)})
})