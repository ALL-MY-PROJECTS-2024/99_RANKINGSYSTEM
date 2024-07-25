const thumb_up_btn = document.querySelector('.thumb_up_btn');

    thumb_up_btn.addEventListener('click',function(){
        console.log("clicked..thumb_up");

        const imageid = thumb_up_btn.getAttribute('data-imageid');

        axios.get(`/favorite/music/${imageid}`)
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


const bookmark_btn = document.querySelector('.bookmark_btn');

    bookmark_btn.addEventListener('click',function(){
        console.log("clicked..bookmark_btn");

        const imageid = thumb_up_btn.getAttribute('data-imageid');

        axios.get(`/bookmark/add/music/${imageid}`)
        .then(resp=>{
               console.log(resp);
               if(resp.data.exist=='false'){
                    alert("즐겨찾기 등록 완료");
               }else{
                    alert("이미 추가 되었습니다.")
               }

         })
        .catch(err=>{console.log(err);})

    })

//--------------------------------------
// 음악 재생
//--------------------------------------
const audio = document.querySelector('#audio');

//audio.play();
const music_play_btn=document.querySelector('.music_play_btn');
music_play_btn.addEventListener('click',function(){
     audio.play();
})
const music_stop_btn=document.querySelector('.music_stop_btn');
music_stop_btn.addEventListener('click',function(){
     audio.pause();
})