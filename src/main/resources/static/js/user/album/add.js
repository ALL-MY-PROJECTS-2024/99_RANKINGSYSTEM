
//---------------------------------------------------
//---------------------------------------------------

//---------------------------------------------------
//---------------------------------------------------
//---------------------------------------------------
// IMAGE UPLOAD EVENT
//---------------------------------------------------
const formData = new FormData();	//폼관련 정보 저장

const uploadBoxEl = document.querySelector('.upload-box');
uploadBoxEl.addEventListener('dragover',function(event){
        console.log('dragover')
        event.preventDefault();

});
uploadBoxEl.addEventListener('dragleave',function(event){
        console.log('dragleave')
        event.preventDefault();
});

uploadBoxEl.addEventListener('drop',function(e){

    console.log('uploadBoxEl drop..',event.dataTransfer.files[0]);

    e.preventDefault();
    console.log("drop...");
    console.log(e.dataTransfer.files[0]);

   //유효성 체크 filter , map
    const imgFiles= Array.from(e.dataTransfer.files).filter(f=> f.type.startsWith('image/'));
    if(imgFiles.length===0){
        alert("이미지 파일만 가능합니다.")
        return false;
    }
    //이미지의 개수 5개 제한
    //이미지 하나당 사이즈 제한..
    imgFiles.forEach(file=>{
        if(file.size>(1024*1024*5)){
             alert("파일하나당 최대 사이즈는 5Mb이하여야 합니다..")
             return false;
         }
    })
     for(var file of imgFiles ){
            const reader = new FileReader();
            reader.readAsDataURL(file);

            reader.onload=function(e){
                const preview = document.querySelector('#preview');
                const imgEl =  document.createElement('img');
    //          console.log("reader.onload",e)
                imgEl.setAttribute('src',e.target.result);
                preview.appendChild(imgEl);
            }
            formData.append('files',file);
            console.log("formData",formData);
        }
});


const add_product_btn_el = document.querySelector('.add_album_btn');
        add_product_btn_el.addEventListener('click',function(){


        const username = document.albumform.username.value;
        const title = document.albumform.title.value;
        const main_category = document.albumform.main_category.value;

        //선택되어진 sub_category 를 확인

        const sub_categoryEls = document.querySelectorAll('.sub_category');
        let sub_category = '';
        sub_categoryEls.forEach(el=>{
            if(!el.classList.contains('hidden'))
                sub_category = el.querySelector('select').value;
        })
        const description = document.albumform.description.value;


        formData.append('username',username);
        formData.append('title',title);
        formData.append('mainCategory',main_category);
        formData.append('subCategory',sub_category);
        formData.append('description',description);

        axios.post('/user/album/add',formData,{ headers: {'Content-Type' :'multipart/form-data' } } )
                .then(res=>{
                    console.log(res);
                    alert("업로드 완료")
                    location.href="/user/album/main";
                })
                .catch(err=>{console.log(err);})

});



//---------------------------------------------------
//MAINCAT -> SUBCAT 변경
//---------------------------------------------------
const mainCategoryEl = document.querySelector('.main_category');
const subImageCatEl = document.querySelector('.sub_category.image-cat');
const subMusicCatEl = document.querySelector('.sub_category.music-cat');

mainCategoryEl.addEventListener('change',()=>{

    console.log(mainCategoryEl.value);

    if(mainCategoryEl.value == 'image'){

        subImageCatEl.classList.remove('hidden');
        subMusicCatEl.classList.add('hidden');

    }else if(mainCategoryEl.value == 'music'){

        subImageCatEl.classList.add('hidden');
        subMusicCatEl.classList.remove('hidden');
    }

})