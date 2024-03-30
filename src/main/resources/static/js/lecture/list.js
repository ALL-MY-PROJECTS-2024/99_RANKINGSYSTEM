const bgEls = document.querySelectorAll('.bg');
bgEls.forEach(el=>{
    el.addEventListener('click',function(){
        console.log('clicked...');

        const modalBtnEl = document.querySelector('.modalbtn');

        const modalBodyEl = document.querySelector('.modal-body');

        while(modalBodyEl.firstChild)
            modalBodyEl.removeChild(modalBodyEl.firstChild);



        const iframeEl =  el.nextElementSibling;


        const copyEl =  iframeEl.cloneNode(true);

        copyEl.src=copyEl.src+"?muted=1&autoplay=1";

        copyEl.width="100%";
        copyEl.height="500";
        console.log(copyEl);
        modalBodyEl.appendChild(copyEl);

        modalBtnEl.click();
    })
})