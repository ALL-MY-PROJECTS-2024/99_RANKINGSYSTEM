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
const nextBtn = document.querySelector('.next_btn');
if(nextBtn!=null){
    nextBtn.addEventListener('click',function(){
        console.log('clicked..');
        const showBlock = document.querySelector('.show-block');
        const pageNo =  nextBtn.getAttribute('data-pageNo');
        //mode  ,pageNo,mainCat , subCat,amount
        axios.get(`/imageRanking/cat/next?subCategory=${subCategory}&pageNo=${pageNo}&amount=${amount}`)
        .then(resp=>{
            console.log(resp);
             resp.data.imageList.forEach(el=>{
                    createNode(el,showBlock);
             });

            if(resp.data.pageDto.next)
                nextBtn.setAttribute('data-pageNo',resp.data.pageDto.criteria.pageno + 1);
            else
                nextBtn.style.display='none';
        })
        .catch(err=>{console.log(err);})
    })
}
function createNode(el,parent){
    const div = document.createElement('div');
    div.classList.add('show-image-block');

    const a = document.createElement('a');
    a.setAttribute("href",`/imageRanking/read?rankingId=${el.rankingId}` )
    const img = document.createElement('img');
    img.setAttribute('src',el.imagesFileInfo.dir+'/'+el.imagesFileInfo.filename);
    a.appendChild(img);
    div.appendChild(a);
    parent.appendChild(div);
}

