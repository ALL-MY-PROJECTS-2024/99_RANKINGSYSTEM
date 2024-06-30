//----------------------
// SWIPER
//-----------------------
const imagesBlock = new Swiper('.images-block .swiper', {
        loop: false,
        centeredSlides: false,
        slidesPerView : 5,
        autoplay: {
            delay: 10000,
            disableOnInteraction: false
        },
      navigation: {
        nextEl: ".swiper-button-next",
        prevEl: ".swiper-button-prev"
      },
});
