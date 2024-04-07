//        const swiper_vertical = new Swiper('.banner .swiper', {
//        direction: 'vertical',
//        autoplay:{
//            delay:1000,
//        },
//        loop: true,
//        });

        const swiper_banner = new Swiper('.banner .swiper', {
        direction: 'horizontal',
        autoplay:{
            delay:3000,

            pauseOnMouseEnter:true
        },
        loop: true,
            // If we need pagination
            pagination: {
                el: '.swiper-pagination',
                clickable:'true',
                type:'bullets', //bullets,progressbar,fraction
            },

            // Navigation arrows
            navigation: {
                nextEl: '.swiper-button-next',
                prevEl: '.swiper-button-prev',
            },

            // And if we need scrollbar
            scrollbar: {
                el: '.swiper-scrollbar',
            },
            mousewheel:false,
            // effect:'fade',
        });

//
//        const swiper_multi_slider = new Swiper('.three_section .swiper', {
//        direction: 'horizontal',
//        autoplay:{
//            delay:1000,
//        },
//        loop: true,
//        slidesPerView:4 ,
//        centeredSlides:true,
//
//        });\


//----------------------
// SCROLL MAGIC
//-----------------------
const spyEls = document.querySelectorAll('section.scroll-spy');
        let cnt =0;
        spyEls.forEach(function(spyEl){

            console.log("spyEl",spyEl);
            if(cnt==0){
                // 스크롤매직이벤트처리 객체
                new ScrollMagic.Scene({
                    triggerElement : spyEl, //감시할 요소 선택
                    triggerHook : 0.5       //스크린에서의 스크롤 위치
                })
                .setClassToggle(spyEl,'show')
                .addTo(new ScrollMagic.Controller());
                cnt++;
            }else{
                // 스크롤매직이벤트처리 객체
                new ScrollMagic.Scene({
                    triggerElement : spyEl, //감시할 요소 선택
                    triggerHook : 0.5       //스크린에서의 스크롤 위치
                })
                .setClassToggle(spyEl,'show2')
                .addTo(new ScrollMagic.Controller());
            }
 })


