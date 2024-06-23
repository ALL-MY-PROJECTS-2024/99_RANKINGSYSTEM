
const cancelEls = document.querySelectorAll('.cancel>a');

cancelEls.forEach(el=>{
    el.addEventListener('click',()=>{

        const id = el.getAttribute('data-id');
        axios.get(`/bookmark/del/${id}`)
        .then(resp=>{
            console.log(resp);
            alert("즐겨찾기에서 제거했습니다.");
            location.href="/bookmark/my";
        })
        .catch(err=>{console.log(err);})
    })
})

//--------------------------------
//이미지 / 음악 버튼 누를시 반응
//--------------------------------
//음악 / 이미지 버튼
const btnImg = document.querySelector('.my-btn.btn-img');
const btnMusic = document.querySelector('.my-btn.btn-music');

const imageBlock = document.querySelector('.image-block.items');
const musicBlock = document.querySelector('.music-block.items');

btnImg.addEventListener('click',function(){

    btnImg.classList.add('active')
    btnMusic.classList.remove('active');

    imageBlock.classList.remove('hidden');
    musicBlock.classList.add('hidden');

})
btnMusic.addEventListener('click',function(){
    btnMusic.classList.add('active')
    btnImg.classList.remove('active');

    musicBlock.classList.remove('hidden');
    imageBlock.classList.add('hidden');

})
