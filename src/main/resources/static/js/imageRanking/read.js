const thumb_up_btn = document.querySelector('.thumb_up_btn');

    thumb_up_btn.addEventListener('click',function(){
        console.log("clicked..thumb_up");

        const imageid = thumb_up_btn.getAttribute('data-imageid');

        axios.get(`/favorite/image/${imageid}`)
        .then(resp=>{
               console.log(resp);
               if(!resp.data.favorite){
                    alert("좋아요 등록 완료");
               }else{
                    alert("좋아요 제거 완료")
               }

         })
        .catch(err=>{console.log(err);})

    })
