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