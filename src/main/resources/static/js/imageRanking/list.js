
const thumb_up_btn_els = document.querySelectorAll('.thumb_up_btn');

thumb_up_btn_els.forEach((el)=>{
    el.addEventListener('click',function(){
        console.log("clicked..thumb_up");

        const imageid = el.getAttribute('data-imageid');
        const thumbUpCounter = el.querySelector('.thumb-up-counter');
        const thumbUpIcon = el.querySelector('.material-symbols-outlined.favorite')

        axios.get(`/favorite/image/${imageid}`)
        .then(resp=>{
            console.log(resp);
           const isFavorite = resp.data.favorite;
           if(isFavorite){
                thumbUpIcon.setAttribute('style',"font-variation-settings:'FILL' 0,'wght' 300,'GRAD' 0,'opsz' 18");
           }else{
                thumbUpIcon.setAttribute('style',"font-variation-settings:'FILL' 100,'wght' 300,'GRAD' 0,'opsz' 18");
           }

           thumbUpCounter.innerHTML =  resp.data.count;

         })
        .catch(err=>{console.log(err);})

    })
})
