//---------------------------------------------------
//초기모드
//---------------------------------------------------
const imageSubButtonInitEls = document.querySelectorAll('.image_body .swiper-slide a');
imageSubButtonInitEls.forEach(el=>{
            el.classList.remove('active');
            const title = el.getAttribute('data-title');

            if(subCategory==null){
                el.classList.add('active')
            }else if(title == subCategory){
                el.classList.add('active')
            }
})

//---------------------------------------------------
//NEXT 버튼클릭시
//---------------------------------------------------
const musicNextBtn = document.querySelector('.music_next_btn');
if(musicNextBtn!=null){
    musicNextBtn.addEventListener('click',function(){
        console.log('clicked..');
        const showBlock = document.querySelector('.show-block.music-block');
        const pageNo =  musicNextBtn.getAttribute('data-pageNo');
        //mode  ,pageNo,mainCat , subCat,amount
        axios.get(`/musicRanking/cat/next?subCategory=${subCategory}&pageNo=${pageNo}&amount=${amount}`)
        .then(resp=>{
            console.log(resp);
             resp.data.musicList.forEach(el=>{
                    createMusicNode(el,showBlock);
             });
            if(resp.data.pageDto.next)
                musicNextBtn.setAttribute('data-pageNo',resp.data.pageDto.criteria.pageno + 1);
            else
                musicNextBtn.style.display='none';
        })
        .catch(err=>{console.log(err);})
    })
}
function createMusicNode(el,parent){
    const div = document.createElement('div');
    div.classList.add('show-image-block');

    const a = document.createElement('a');
    a.setAttribute("href",`/musicRanking/read?rankingId=${el.rankingId}` )
    const img = document.createElement('img');
    img.setAttribute('src',el.musicFileInfo.dir+'/'+el.musicFileInfo.albumImageName);
    a.appendChild(img);
    div.appendChild(a);
    parent.appendChild(div);
}

