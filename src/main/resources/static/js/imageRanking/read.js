const thumb_up_btn = document.querySelector('.thumb_up_btn');

    thumb_up_btn.addEventListener('click',function(){
        console.log("clicked..thumb_up");

        const imageid = thumb_up_btn.getAttribute('data-imageid');

        axios.get(`/favorite/image/${imageid}`)
        .then(resp=>{
               console.log(resp);
               if(!resp.data.favorite){
                    alert("좋아요 등록 완료");
               }else{
                    alert("좋아요 제거 완료")
               }

         })
        .catch(err=>{console.log(err);})

    })


const bookmark_btn = document.querySelector('.bookmark_btn');

    bookmark_btn.addEventListener('click',function(){
        console.log("clicked..bookmark_btn");

        const imageid = thumb_up_btn.getAttribute('data-imageid');

        axios.get(`/bookmark/add/${imageid}`)
        .then(resp=>{
               console.log(resp);
               if(resp.data.exist=='false'){
                    alert("즐겨찾기 등록 완료");
               }else{
                    alert("이미 추가 되었습니다.")
               }

         })
        .catch(err=>{console.log(err);})

    })


//--------------------------------
//댓글
//--------------------------------
const inputBlock = document.querySelector('.reply-block input');

const replyBlock = document.querySelector('.reply-block');
replyBlock.addEventListener('keydown',(e)=>{
    if(inputBlock.value!=null && inputBlock.value!=''){
        if(e.keyCode==13){
          console.log('data : ' + inputBlock.value);

          const context = inputBlock.value;
          axios.get(`/imageRanking/reply/add?context=${context}&imageId=${imageId}`)
          .then((resp)=>{
            console.log(resp);
            //createReplyNode(resp.data)
            location.reload();

          })
          .catch((err)=>{console.log(err);})


          inputBlock.value='';
        }
    }
})
const replyIcon = document.querySelector('.reply-icon');
replyIcon.addEventListener('click',()=>{
    if(inputBlock.value!=null && inputBlock.value!=''){
        console.log('data : ' + inputBlock.value);

          const context = inputBlock.value;
          axios.get(`/imageRanking/reply/add?context=${context}&imageId=${imageId}`)
          .then((resp)=>{
            console.log(resp);
            //createReplyNode(resp.data)
            location.reload();
          })
          .catch((err)=>{console.log(err);})

        inputBlock.value='';

    }
})
//--------------------------------------------------------
//댓글삭제
//--------------------------------------------------------
const replyDeleteBtns= document.querySelectorAll('.replyDeleteBtn');
replyDeleteBtns.forEach(btn=>{

    btn.addEventListener('click',()=>{
        const id = btn.getAttribute('data-id');
        axios.delete(`/imageRanking/reply/delete?id=${id}`)
        .then(resp=>{
            console.log(resp)
            alert(resp.data);
            location.reload();
        })
        .catch(err=>{
            console.log(err);
            alert(err.response.data);
        })
    })

})



//function createReplyNode(data){
//    // Create elements
//    const item = document.createElement('div');
//    item.className = 'item';
//
//    const left = document.createElement('div');
//    left.className = 'left';
//
//    const img = document.createElement('img');
//    img.src = data.user.profileImage;
//    img.alt = '';
//    left.appendChild(img);
//
//    const right = document.createElement('div');
//    right.className = 'right';
//
//    const one = document.createElement('div');
//    one.className = 'one';
//
//    const user = document.createElement('span');
//    user.className = 'user';
//    user.textContent = data.user.username
//
//    const date = document.createElement('span');
//    date.className = 'date';
//    date.textContent = formatDate(data.date)
//
//    one.appendChild(user);
//    one.appendChild(date);
//
//    const two = document.createElement('div');
//    two.className = 'two';
//
//    const textarea = document.createElement('textarea');
//    textarea.className = 'context';
//    textarea.readOnly = true;
//    textarea.textContent = data.context;
//
//    two.appendChild(textarea);
//
//    right.appendChild(one);
//    right.appendChild(two);
//
//    item.appendChild(left);
//    item.appendChild(right);
//
//    const contents = document.querySelector('.contents');
//    contents.insertBefore(item, contents.firstChild);
//}

//
//function formatDate(arr) {
//    // 배열의 각 요소를 변수로 추출
//    const [year, month, day, hours, minutes, seconds, milliseconds] = arr;
//
//    // Date 객체 생성 (월은 0부터 시작하므로 month - 1)
//    const date = new Date(year, month - 1, day, hours, minutes, seconds, milliseconds);
//
//    // 원하는 형식으로 변환
//    const formattedYear = date.getFullYear();
//    const formattedMonth = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
//    const formattedDay = String(date.getDate()).padStart(2, '0');
//    const formattedHours = String(date.getHours()).padStart(2, '0');
//    const formattedMinutes = String(date.getMinutes()).padStart(2, '0');
//    const formattedSeconds = String(date.getSeconds()).padStart(2, '0');
//
//    return `${formattedYear}-${formattedMonth}-${formattedDay} ${formattedHours}:${formattedMinutes}:${formattedSeconds}`;
//}

