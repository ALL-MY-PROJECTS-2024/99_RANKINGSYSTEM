//---------------------------------------------------
//이미지 / 음악  라벨 변경시 효과 적용
//---------------------------------------------------
const imagebtn = document.querySelector('.group-block>.head>div:nth-child(1) label');
const musicbtn = document.querySelector('.group-block>.head>div:nth-child(2) label');

const imagebody = document.querySelector('.group-block>.body .image_body');
const musicbody = document.querySelector('.group-block>.body .music_body');


//imagebtn.addEventListener('click',function(){
//    console.log("imagebtn clicked..");
//
//    imagebody.classList.remove('hidden');
//    musicbody.classList.add('hidden');
//
//    imagebtn.style.opacity="1";
//    musicbtn.style.opacity=".8";
//
//})


imagebtn.style.opacity=".8";
musicbtn.style.opacity="1";
musicbtn.addEventListener('click',function(){
    console.log("musicbtn clicked..");

    imagebody.classList.add('hidden');
    musicbody.classList.remove('hidden');

    imagebtn.style.opacity=".8";
    musicbtn.style.opacity="1";
})
//---------------------------------------------------
//이미지 / 음악 서브카테고리 이벤트 처리
//---------------------------------------------------

//const imageSubButtonEls = document.querySelectorAll('.image_body .swiper-slide a');
//let subCategoryBodyImgTitleEl = document.querySelector('.image_body .subCategoryBody .title');
//
//imageSubButtonEls.forEach(el=>{
//    el.addEventListener('click',function(){
//        console.log(el.innerHTML);
//
//        subCategoryBodyImgTitleEl.innerHTML=el.innerText;
//    })
//})


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

