
const imageBodyNextBtn = document.querySelector('.image_body .swiper-button-next')
imageBodyNextBtn.addEventListener('click',function(){
    const swiperWrapper = document.querySelector('.image_body .swiper-wrapper')
    swiperWrapper.style.left="-60%";
})
const imageBodyPrevBtn = document.querySelector('.image_body .swiper-button-prev')
imageBodyPrevBtn.addEventListener('click',function(){
    const swiperWrapper = document.querySelector('.swiper-wrapper')
    swiperWrapper.style.left="0";
})


//---------------------------------------------------
//초기모드
//---------------------------------------------------
const imageSubButtonInitEls = document.querySelectorAll('.image_body .swiper-slide a');
const musicSubButtonInitEls = document.querySelectorAll('.music_body .swiper-slide a');
subCategory = subCategory.replaceAll('\'','').trim();
console.log("!!",subCategory);
if(mode=='1'){
    imageSubButtonInitEls.forEach(el=>{
            el.classList.remove('active');
            const title = el.getAttribute('data-title');

            if(subCategory==null && title=='Character'){
                el.classList.add('active')
            }else if(title == subCategory){
                el.classList.add('active')
            }
    })
}else{
    musicSubButtonInitEls.forEach(el=>{
            el.classList.remove('active');
            const title = el.getAttribute('data-title');

            if(subCategory==null && title=='Jazz'){
                el.classList.add('active')
            }else if(title == subCategory){
                el.classList.add('active')
            }
    })
}


//---------------------------------------------------
//이미지 / 음악  라벨 변경시 효과 적용
//---------------------------------------------------
const imagebtn = document.querySelector('.group-block>.head>div:nth-child(1) label');
const musicbtn = document.querySelector('.group-block>.head>div:nth-child(2) label');

const imagebody = document.querySelector('.group-block>.body .image_body');
const musicbody = document.querySelector('.group-block>.body .music_body');


imagebtn.addEventListener('click',function(){
    console.log("imagebtn clicked..");

    imagebody.classList.remove('hidden');
    musicbody.classList.add('hidden');

    imagebtn.style.opacity="1";
    musicbtn.style.opacity=".8";

    location.href="/around/group?mainCategory='이미지'&subCategory=Character&mode=1";
})
musicbtn.addEventListener('click',function(){
    console.log("musicbtn clicked..");

    imagebody.classList.add('hidden');
    musicbody.classList.remove('hidden');

    imagebtn.style.opacity=".8";
    musicbtn.style.opacity="1";

    location.href="/around/group?mainCategory='음악'&subCategory=Jazz&mode=2";

})
//---------------------------------------------------
//이미지 / 음악 서브카테고리 이벤트 처리
//---------------------------------------------------

const imageSubButtonEls = document.querySelectorAll('.image_body .swiper-slide a');
let subCategoryBodyImgTitleEl = document.querySelector('.image_body .subCategoryBody .title');

imageSubButtonEls.forEach(el=>{
    el.addEventListener('click',function(){
        console.log(el.innerHTML);
        subCategoryBodyImgTitleEl.innerHTML=el.innerText;
    })
})


//
const musicSubButtonEls = document.querySelectorAll('.music_body .swiper-slide a');
let subCategoryBodyMusicTitleEl = document.querySelector('.music_body .subCategoryBody .title');

musicSubButtonEls.forEach(el=>{
    el.addEventListener('click',function(){
        console.log(el.innerHTML);
        subCategoryBodyMusicTitleEl.innerHTML=el.innerText;
    })
})


//---------------------------------------------------
//DOM INIT 시 처리 코드
//---------------------------------------------------
document.addEventListener('DOMContentLoaded', function() {
   if(mode=="1"){
       imagebtn.style.opacity="1";
       musicbtn.style.opacity=".8";
   }else{
       imagebtn.style.opacity=".8";
       musicbtn.style.opacity="1";
   }
});

//---------------------------------------------------
//NEXT 버튼클릭시
//---------------------------------------------------
const nextBtn = document.querySelector('.next_btn');
if(nextBtn!=null){
    nextBtn.addEventListener('click',function(){
        console.log('clicked..');
        const showBlock = document.querySelector('.show-block');
        const pageNo =  nextBtn.getAttribute('data-pageNo');
        //mode  ,pageNo,mainCat , subCat,amount
        axios.get(`/around/group/next?mainCategory=${mainCategory}&subCategory=${subCategory}&pageNo=${pageNo}&amount=${amount}`)
        .then(resp=>{
            console.log(resp);


             resp.data.imageList.forEach(el=>{
                    createNode(el,showBlock);
             });

            if(resp.data.pageDto.next)
                nextBtn.setAttribute('data-pageNo',resp.data.pageDto.criteria.pageno + 1);
            else
                nextBtn.style.display='none';
        })
        .catch(err=>{console.log(err);})
    })
}
function createNode(el,parent){
    const div = document.createElement('div');
    div.classList.add('show-image-block');

    const a = document.createElement('a');
    a.setAttribute("href",`/imageRanking/read?rankingId=${el.rankingId}` )
    const img = document.createElement('img');
    img.setAttribute('src',el.imagesFileInfo.dir+'/'+el.imagesFileInfo.filename);
    a.appendChild(img);
    div.appendChild(a);
    parent.appendChild(div);
}

const musicNextBtn = document.querySelector('.music_next_btn');
if(musicNextBtn!=null){
    musicNextBtn.addEventListener('click',function(){
        console.log('clicked..');
        const showBlock = document.querySelector('.show-block.music-block');
        const pageNo =  musicNextBtn.getAttribute('data-pageNo');
        //mode  ,pageNo,mainCat , subCat,amount
        axios.get(`/around/group/next?mainCategory=${mainCategory}&subCategory=${subCategory}&pageNo=${pageNo}&amount=${amount}`)
        .then(resp=>{
            console.log(resp);
             resp.data.musicList.forEach(el=>{
                    createMusicNode(el,showBlock);
             });
            if(resp.data.pageDto.next)
                musicNextBtn.setAttribute('data-pageNo',resp.data.pageDto.criteria.pageno + 1);
            else
                musicNextBtn.style.display='none';
        })
        .catch(err=>{console.log(err);})
    })
}
function createMusicNode(el,parent){
    const div = document.createElement('div');
    div.classList.add('show-image-block');

    const a = document.createElement('a');
    a.setAttribute("href",`/musicRanking/read?rankingId=${el.rankingId}` )
    const img = document.createElement('img');
    img.setAttribute('src',el.musicFileInfo.dir+'/'+el.musicFileInfo.albumImageName);
    a.appendChild(img);
    div.appendChild(a);
    parent.appendChild(div);
}

