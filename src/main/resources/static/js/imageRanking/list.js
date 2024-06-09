
const thumb_up_btn_els = document.querySelectorAll('.thumb_up_btn');

thumb_up_btn_els.forEach((el)=>{
    el.addEventListener('click',function(){
        console.log("clicked..thumb_up");

        const imageid = el.getAttribute('data-imageid');
        const thumbUpCounter = el.querySelector('.thumb-up-counter');
        const thumbUpIcon = el.querySelector('.material-symbols-outlined.favorite')

        axios.get(`/favorite/image/${imageid}`)
        .then(resp=>{
            console.log(resp);
           const isFavorite = resp.data.favorite;
           if(isFavorite){
                thumbUpIcon.setAttribute('style',"font-variation-settings:'FILL' 0,'wght' 300,'GRAD' 0,'opsz' 18");
           }else{
                thumbUpIcon.setAttribute('style',"font-variation-settings:'FILL' 100,'wght' 300,'GRAD' 0,'opsz' 18");
           }

           thumbUpCounter.innerHTML =  resp.data.count;

         })
        .catch(err=>{console.log(err);})

    })
})


//----------------------------------------------------------------
// 내가 누른 좋아요 확인
//----------------------------------------------------------------
const favoriteBlock = document.querySelectorAll('.favorite-block');

favoriteBlock.forEach(el=>{
    const rankingId =  el.getAttribute('data-rankingid');

    favoriteList.forEach(myFavorite=>{

        if(rankingId==myFavorite.imagesRanking.rankingId){

            const thumbUp =  el.querySelector('.favorite');
            thumbUp.setAttribute('style',"font-variation-settings:'FILL' 100,'wght' 300,'GRAD' 0,'opsz' 18");

        }

    })
})

//----------------------------------------------------------------
// 즐겨찾기 추가
//----------------------------------------------------------------

const bookmarkBtnEls = document.querySelectorAll('.bookmark_btn');
bookmarkBtnEls.forEach(el=>{

    el.addEventListener('click',()=>{

           const rankingId =  el.getAttribute('data-id');
           axios.get(`/bookmark/add/${rankingId}`)
           .then(resp=>{
                console.log(resp);
                if(resp.data.exist=='true'){
                    alert("이미 즐겨찾기에 추가되어 있습니다.");
                }
                else if(resp.data.exist=='false'){
                    alert("즐겨찾기에 추가했습니다.");
                }
           })
           .catch(err=>{console.log(err)});


    })
})
//----------------------------------------------------------------
// .rank 숫자 처리
//----------------------------------------------------------------
const rankEls = document.querySelectorAll('.rank');

let i = pageDto.startPage;
rankEls.forEach(el=>{
    el.innerHTML=i;
    i++;
})