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