
//---------------------------------------------------
//sub_category Swiper
//---------------------------------------------------
//var section1_1 = new Swiper('.image_body  .sub_category.swiper',
//{
//                        slidesPerView: 8,
//                        initialSlide: 0,
//                        centeredSlides: false, // 가운데 정렬 해제
//                        spaceBetween: 10, // 슬라이드 사이의 간격 설정
//                        navigation: {
//                          nextEl: '.image_body  .swiper-button-next',
//                          prevEl: '.image_body  .swiper-button-prev',
//                        }
//});


const imageBodyNextBtn = document.querySelector('.image_body .swiper-button-next')
imageBodyNextBtn.addEventListener('click',function(){
    const swiperWrapper = document.querySelector('.swiper-wrapper')
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
imageSubButtonInitEls.forEach(el=>{
            el.classList.remove('active');
            const title = el.getAttribute('data-title');

            if(subCategory==null){
                el.classList.add('active')
            }else if(title == subCategory){
                el.classList.add('active')
            }
})


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
        axios.get(`/imageRanking/cat/next?subCategory=${subCategory}&pageNo=${pageNo}&amount=${amount}`)
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

