//----------------------
// SWIPER
//-----------------------
const imagesBlock = new Swiper('.images-block .swiper', {
        loop: true,
        initialSlide: 1,
        //centeredSlides: true,
        speed: 10000,
        autoplay: {
            delay: 0,
            disableOnInteraction: false
        },
        slidesPerView: 'auto',

    });
