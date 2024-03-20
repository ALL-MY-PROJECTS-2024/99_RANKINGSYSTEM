
//---------------------------------------------------
//---------------------------------------------------
//---------------------------------------------------
//---------------------------------------------------
//---------------------------------------------------
// IMAGE UPLOAD EVENT
//---------------------------------------------------
    const uploadBoxEl = document.querySelector('.upload-box');
    uploadBoxEl.addEventListener('dragover',function(event){
        console.log('dragover')
        event.preventDefault();

    });
    uploadBoxEl.addEventListener('dragleave',function(event){
        console.log('dragleave')
        event.preventDefault();

    });
    uploadBoxEl.addEventListener('drop',function(event){

    console.log('uploadBoxEl drop..',event.dataTransfer.files[0]);
    const files =event.dataTransfer.files;
    if(files[0].type.startsWith("image/")){

        const previewEl = document.querySelector('.preview_img_block #preview');
        //
        let imageEl = document.createElement('img');
        imageEl.setAttribute('style','width:50px;height:50px;border:1px solid;')
        imageEl.src=URL.createObjectURL(files[0]);
        //
        var reader = new FileReader();
        reader.readAsDataURL(files[0]);
        reader.onload = function(e){
            imageEl =  e.target.result;
        }
        //
        previewEl.appendChild(imageEl);
        console.log("reader's result!", imageEl);
    }
    event.preventDefault();
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