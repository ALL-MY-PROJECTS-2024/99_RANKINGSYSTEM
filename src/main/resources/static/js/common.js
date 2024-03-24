let rightText = document.querySelector("right-2");

window.addEventListener("scroll", function () {
    let value = window.scrollY;
    if(value>800){
        rightText.style.animation = "appear 1s ease-in";
    }
});

