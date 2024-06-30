//---------------------------------------------------
//이미지 / 음악  라벨 변경시 효과 적용
//---------------------------------------------------
const imagebtn = document.querySelector('.group-block>.head>div:nth-child(1) label');
const musicbtn = document.querySelector('.group-block>.head>div:nth-child(2) label');
musicbtn.style.opacity="1";
imagebtn.style.opacity=".8";


//---------------------------------------------------
//이미지 / 음악 서브카테고리 이벤트 처리
//---------------------------------------------------




const JazzBlock = document.querySelector('.items.Jazz-block');
const RockBlock = document.querySelector('.items.Rock-block');
const ClassicBlock = document.querySelector('.items.Classic-block');
const ProgressiveBlock = document.querySelector('.items.Progressive-block');
const AdvertisementBlock = document.querySelector('.items.Advertisement-block');
const HeavyMetalBlock = document.querySelector('.items.HeavyMetal-block');
const PopBlock = document.querySelector('.items.Pop-block');
const OthersBlock = document.querySelector('.items.Others-block');

createElement(JazzBlock,Jazz);
createElement(RockBlock,Rock);
createElement(ClassicBlock,Classic);
createElement(ProgressiveBlock,Progressive);
createElement(AdvertisementBlock,Advertisement);
createElement(HeavyMetalBlock,HeavyMetal);
createElement(PopBlock,Pop);
createElement(OthersBlock,Others);

function createElement(parent,array){
    array.forEach(el =>{
        const itemEl = document.createElement('div');
        itemEl.classList.add('item');
        const img = document.createElement('img');
        img.setAttribute('src','/assets/music.jpg');
        img.setAttribute('style','height:100px;')

        const com1El = document.createElement('div');
        com1El.innerHTML="제목 : "+el.musicFileInfo.music.title;
        const com2El = document.createElement('div');
        com2El.innerHTML="조회수 : "+el.count;
        const com3El = document.createElement('div');
        com3El.innerHTML="좋아요 수 : "+el.ilikeit;

        com1El.setAttribute('style','height:25px;line-height:25px;margin-bottom:2px;font-size:.7rem;text-align:center;')
        com2El.setAttribute('style','height:25px;line-height:25px;margin-bottom:2px;font-size:.7rem;text-align:center;')
        com3El.setAttribute('style','height:25px;line-height:25px;margin-bottom:2px;font-size:.7rem;text-align:center;')

        itemEl.appendChild(img);
        itemEl.appendChild(com1El);
        itemEl.appendChild(com2El);
        itemEl.appendChild(com3El);
        parent.appendChild(itemEl);
    })
}
//---------------------------------------------------
//서브버튼 이벤트 처리
//---------------------------------------------------
const imageSubButtonEls = document.querySelectorAll('.music_body .swiper-slide a');
let subCategoryBodyImgTitleEl = document.querySelector('.music_body .subCategoryBody .title');
imageSubButtonEls.forEach(el=>{
    el.addEventListener('click',function(){
        console.log(el.innerHTML);
        const subCat = el.innerHTML;
        ElementHidden(subCat);
    })
})


function ElementHidden(except){
         JazzBlock.classList.remove('active');
         RockBlock.classList.remove('active');
         ClassicBlock.classList.remove('active');
         ProgressiveBlock.classList.remove('active');
         AdvertisementBlock.classList.remove('active');
         HeavyMetalBlock.classList.remove('active');
         PopBlock.classList.remove('active');
         OthersBlock.classList.remove('active');
         if(except==="Jazz")
            JazzBlock.classList.add('active');
         else if(except==="Rock")
            RockBlock.classList.add('active');
          else  if(except==="Classic")
             ClassicBlock.classList.add('active');
          else  if(except==="Progressive")
             ProgressiveBlock.classList.add('active');
          else if(except==="Advertisement")
             AdvertisementBlock.classList.add('active');
          else if(except==="HeavyMetal")
             HeavyMetalBlock.classList.add('active');
          else if(except==="Pop")
             PopBlock.classList.add('active');
          else if(except==="Others")
             OthersBlock.classList.add('active');
}




