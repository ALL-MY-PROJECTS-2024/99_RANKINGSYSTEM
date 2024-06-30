//---------------------------------------------------
//이미지 / 음악  라벨 변경시 효과 적용
//---------------------------------------------------
const imagebtn = document.querySelector('.group-block>.head>div:nth-child(1) label');
const musicbtn = document.querySelector('.group-block>.head>div:nth-child(2) label');
imagebtn.style.opacity="1";
musicbtn.style.opacity=".8";


//---------------------------------------------------
//이미지 / 음악 서브카테고리 이벤트 처리
//---------------------------------------------------




const CharacterBlock = document.querySelector('.items.Character-block');
const CityBlock = document.querySelector('.items.City-block');
const MapBlock = document.querySelector('.items.Map-block');
const iCONBlock = document.querySelector('.items.iCON-block');
const FurnitureBlock = document.querySelector('.items.Furniture-block');
const CarBlock = document.querySelector('.items.Car-block');
const PlaceBlock = document.querySelector('.items.Place-block');
const OthersBlock = document.querySelector('.items.Others-block');

createElement(CharacterBlock,Character);
createElement(CityBlock,City);
createElement(MapBlock,Map);
createElement(iCONBlock,iCON);
createElement(FurnitureBlock,Furniture);
createElement(CarBlock,Car);
createElement(PlaceBlock,Place);
createElement(OthersBlock,Others);

function createElement(parent,array){
    array.forEach(el =>{
        const itemEl = document.createElement('div');
        itemEl.classList.add('item');
        const img = document.createElement('img');
        img.setAttribute('src',el.imagesFileInfo.dir+'/'+el.imagesFileInfo.filename);

        itemEl.appendChild(img);
        parent.appendChild(itemEl);
    })
}
//---------------------------------------------------
//서브버튼 이벤트 처리
//---------------------------------------------------
const imageSubButtonEls = document.querySelectorAll('.image_body .swiper-slide a');
let subCategoryBodyImgTitleEl = document.querySelector('.image_body .subCategoryBody .title');
imageSubButtonEls.forEach(el=>{
    el.addEventListener('click',function(){
        console.log(el.innerHTML);
        const subCat = el.innerHTML;
        ElementHidden(subCat);
    })
})


function ElementHidden(except){
         CharacterBlock.classList.remove('active');
         CityBlock.classList.remove('active');
         MapBlock.classList.remove('active');
         iCONBlock.classList.remove('active');
         FurnitureBlock.classList.remove('active');
         CarBlock.classList.remove('active');
         PlaceBlock.classList.remove('active');
         OthersBlock.classList.remove('active');
         if(except==="Character")
            CharacterBlock.classList.add('active');
         else if(except==="City")
            CityBlock.classList.add('active');
          else  if(except==="Map")
             MapBlock.classList.add('active');
          else  if(except==="iCON")
             iCONBlock.classList.add('active');
          else if(except==="Furniture")
             FurnitureBlock.classList.add('active');
          else if(except==="Car")
             CarBlock.classList.add('active');
          else if(except==="Place")
             PlaceBlock.classList.add('active');
          else if(except==="Others")
             OthersBlock.classList.add('active');

}




