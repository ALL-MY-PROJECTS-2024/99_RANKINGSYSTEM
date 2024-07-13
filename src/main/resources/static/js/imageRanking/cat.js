//---------------------------------------------------
//이미지 / 음악  라벨 변경시 효과 적용
//---------------------------------------------------
const imagebtn = document.querySelector('.group-block>.head>div:nth-child(1) label');
const musicbtn = document.querySelector('.group-block>.head>div:nth-child(2) label');
imagebtn.style.opacity="1";
musicbtn.style.opacity=".8";


//---------------------------------------------------
//
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

const objectCnt = document.querySelector('.object-cnt');
objectCnt.innerHTML=Character.length;


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

         const objectCnt = document.querySelector('.object-cnt');
         if(except==="Character"){
            CharacterBlock.classList.add('active');
            objectCnt.innerHTML=Character.length;
           if(Character.length=='0'){
                CharacterBlock.innerHTML="표시할 이미지가 없습니다.";
            }
         }else if(except==="City"){
            CityBlock.classList.add('active');
            objectCnt.innerHTML=City.length;
           if(City.length=='0'){
                CityBlock.innerHTML="표시할 이미지가 없습니다.";
            }
         }else if(except==="Map"){
             MapBlock.classList.add('active');
             objectCnt.innerHTML=Map.length;
            if(Map.length=='0'){
                 MapBlock.innerHTML="표시할 이미지가 없습니다.";
             }
         }else  if(except==="iCON"){
             iCONBlock.classList.add('active');
             objectCnt.innerHTML=iCON.length;
            if(iCON.length=='0'){
                iCONBlock.innerHTML="표시할 이미지가 없습니다.";
            }
         }else if(except==="Furniture"){
             FurnitureBlock.classList.add('active');
            objectCnt.innerHTML=Furniture.length;
           if(Furniture.length=='0'){
                FurnitureBlock.innerHTML="표시할 이미지가 없습니다.";
            }
         }else if(except==="Car"){
             CarBlock.classList.add('active');
             objectCnt.innerHTML=Car.length;
           if(Car.length=='0'){
                CarBlock.innerHTML="표시할 이미지가 없습니다.";
            }
         }else if(except==="Place"){
             PlaceBlock.classList.add('active');
             objectCnt.innerHTML=Place.length;
           if(Place.length=='0'){
                PlaceBlock.innerHTML="표시할 이미지가 없습니다.";
            }
         }else if(except==="Others"){
            OthersBlock.classList.add('active');
            objectCnt.innerHTML=Others.length;
           if(Others.length=='0'){
                OthersBlock.innerHTML="표시할 이미지가 없습니다.";
            }
         }

}




