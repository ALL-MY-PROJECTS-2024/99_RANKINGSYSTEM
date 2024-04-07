const swiper_vertical = new Swiper('.top-header .left .swiper', {
direction: 'vertical',
        autoplay:{
            delay:3000,
        },
        loop: true,
});


let rightText = document.querySelector(".right-2");
window.addEventListener("scroll", function () {
    let value = window.scrollY;

    if(value>400){
        console.log(value);
        rightText.style.animation = "appear 1s ease-out";
        rightText.style.opacity = "1";
    }
});




