
let rightText = document.querySelector(".right-2");

window.addEventListener("scroll", function () {
    let value = window.scrollY;



    if(value>400){
        console.log(value);
        rightText.style.animation = "appear 1s ease-out";
        rightText.style.opacity = "1";
    }
});

