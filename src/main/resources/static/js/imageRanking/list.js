
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
//조회순
const rankEls = document.querySelectorAll('.count-order-block .rank');

let i = (pageDto.criteria.pageno-1)*pageDto.criteria.amount + 1
rankEls.forEach(el=>{
    el.innerHTML=i;

    i++;
})

//좋아요 순
const likerankEls = document.querySelectorAll('.like-order-block .rank');

let j = (pageDto.criteria.pageno-1)*pageDto.criteria.amount + 1
likerankEls.forEach(el=>{
    el.innerHTML=j;
    j++;
})

//----------------------------------------------------------------
// 좋아요 / 조회순
//----------------------------------------------------------------

const countOrder = document.querySelector(".countOrder");
const likeOrder = document.querySelector(".likeOrder");
const summary = document.querySelector(".summary");

const countOrderBlock = document.querySelector(".count-order-block");
const likeOrderBlock = document.querySelector(".like-order-block");
const summaryBlock = document.querySelector(".summary-block");


countOrder.addEventListener('click',function(){
    console.log('clicked...');
    countOrderBlock.classList.remove('hidden');
    likeOrderBlock.classList.add("hidden");
    summaryBlock.classList.add('hidden');

    countOrder.classList.add('active');
    likeOrder.classList.remove('active');
    summary.classList.remove('active');
});

likeOrder.addEventListener('click',function(){
    console.log('clicked...')
    likeOrderBlock.classList.remove("hidden");;
    countOrderBlock.classList.add('hidden');
    summaryBlock.classList.add('hidden');

    likeOrder.classList.add('active');
    countOrder.classList.remove('active');
    summary.classList.remove('active');
})

summary.addEventListener('click',function(){
    summaryBlock.classList.remove('hidden');
    countOrderBlock.classList.add('hidden');
    likeOrderBlock.classList.add("hidden");


    summary.classList.add('active');
    likeOrder.classList.remove('active');
    countOrder.classList.remove('active');

})
