const imageDownloadBtnEls=document.querySelectorAll('.download-btn');
imageDownloadBtnEls.forEach(el=>{

    el.addEventListener('click',function(){

        const path = el.getAttribute("data-path");
        console.log('cilcked..',path);
        location.href="/user/myinfo/trading/download?filepath="+encodeURIComponent(path);
    })

})