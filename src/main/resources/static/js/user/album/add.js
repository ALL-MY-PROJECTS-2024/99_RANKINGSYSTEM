

//---------------------------------------------------
//좌표가져오기
//---------------------------------------------------
let lat = null;
let lng = null;

function fetchCoordinatesAndUse() {
    window.navigator.geolocation.getCurrentPosition(function(position) {
        lat = position.coords.latitude;
        lng = position.coords.longitude;
        console.log("lat", lat, "lng", lng); // 여기서 좌표를 사용
        // 여기서 좌표를 사용하여 필요한 작업을 수행합니다.
    });
}
//---------------------------------------------------
//---------------------------------------------------
//---------------------------------------------------
// IMAGE UPLOAD EVENT
//---------------------------------------------------
const formData = new FormData();	//폼관련 정보 저장

const uploadBoxEl = document.querySelector('.image_body .upload-box');
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
//                const preview = document.querySelector('#preview');
//                const imgEl =  document.createElement('img');
//    //          console.log("reader.onload",e)
//                imgEl.setAttribute('src',e.target.result);
//                preview.appendChild(imgEl);

                const preview = document.querySelector('.upload-box');
                const imgEl =  document.createElement('img');
    //          console.log("reader.onload",e)
                imgEl.setAttribute('src',e.target.result);
                imgEl.setAttribute('style','position:absolute;width:100%;height:100%;left:0;top:0;object-fit:contain;z-index:10');
                preview.appendChild(imgEl);
            }
            formData.append('files',file);
            console.log("formData",formData);
        }
});


// 좌표 가져오기 및 사용하기
fetchCoordinatesAndUse();

const add_product_btn_el = document.querySelector('.add_album_btn');
        add_product_btn_el.addEventListener('click',function(){

        //업로드 한위치에서의 좌표 가져오기
        fetchCoordinatesAndUse();
        console.log("lat",lat,"lng",lng)


        const username = document.albumform.username.value;
        const title = document.albumform.title.value;
        const main_category = document.albumform.main_category.value;

        //선택되어진 sub_category 를 확인

        const sub_categoryEls = document.querySelectorAll('.sub_category.image-cat');
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
        formData.append('lat',lat);
        formData.append('lng',lng);

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
//const mainCategoryEl = document.querySelector('.main_category');
//const subImageCatEl = document.querySelector('.sub_category.image-cat');
//const subMusicCatEl = document.querySelector('.sub_category.music-cat');
//
//mainCategoryEl.addEventListener('change',()=>{
//
//    console.log(mainCategoryEl.value);
//
//    if(mainCategoryEl.value == 'image'){
//
//        subImageCatEl.classList.remove('hidden');
//        subMusicCatEl.classList.add('hidden');
//
//    }else if(mainCategoryEl.value == 'music'){
//
//        subImageCatEl.classList.add('hidden');
//        subMusicCatEl.classList.remove('hidden');
//    }
//
//})

//---------------------------------------------------
//이미지 / 음악  라벨 변경시 효과 적용
//---------------------------------------------------
const imagebtn = document.querySelector('.content_block>.head>div:nth-child(1) label');
const musicbtn = document.querySelector('.content_block>.head>div:nth-child(2) label');

const imagebody = document.querySelector('.content_block>.body .image_body');
const musicbody = document.querySelector('.content_block>.body .music_body');


imagebtn.addEventListener('click',function(){
    console.log("imagebtn clicked..");

    imagebody.classList.remove('hidden');
    musicbody.classList.add('hidden');

    imagebtn.style.opacity="1";
    musicbtn.style.opacity=".8";

})
musicbtn.addEventListener('click',function(){
    console.log("musicbtn clicked..");

    imagebody.classList.add('hidden');
    musicbody.classList.remove('hidden');

    imagebtn.style.opacity=".8";
    musicbtn.style.opacity="1";
})
//---------------------------------------------------
//음악 앨범 이미지
//---------------------------------------------------
const music_form_data = new FormData();

const musicUploadBoxEl = document.querySelector('.music_body .upload-box');
musicUploadBoxEl.addEventListener('dragover',function(event){
        console.log('dragover')
        event.preventDefault();
});
musicUploadBoxEl.addEventListener('dragleave',function(event){
        console.log('dragleave')
        event.preventDefault();
});

musicUploadBoxEl.addEventListener('drop',function(e){

    console.log('musicUploadBoxEl drop..',event.dataTransfer.files[0]);

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
        if(file.size>(1024*1024*10)){
             alert("파일하나당 최대 사이즈는 5Mb이하여야 합니다..")
             return false;
         }
    })
     for(var file of imgFiles ){
            const reader = new FileReader();
            reader.readAsDataURL(file);

            reader.onload=function(e){
//                const preview = document.querySelector('#preview');
//                const imgEl =  document.createElement('img');
//    //          console.log("reader.onload",e)
//                imgEl.setAttribute('src',e.target.result);
//                preview.appendChild(imgEl);

                const preview = document.querySelector('.music_body .upload-box');
                const imgEl =  document.createElement('img');
    //          console.log("reader.onload",e)
                imgEl.setAttribute('src',e.target.result);
                imgEl.setAttribute('style','position:absolute;width:100%;height:100%;left:0;top:0;');
                preview.appendChild(imgEl);
            }
            music_form_data.append('imageFile',file);

        }
});


//---------------------------------------------------
//뮤직 버튼 클릭시
//---------------------------------------------------
const addMusicBtn = document.querySelector('.add-music-btn');
addMusicBtn.addEventListener('click',function(){

        console.log('clicked..!!');
        const fileForm = /(.*?)\.(mp3|wav)$/;
        var maxSize = 5 * 1024 * 1024;

        const form =  document.musicForm;

        if(form.files.value==""||form.files.value==null){
            alert("음악 파일을 첨부해주세요.")
            return ;
        }
        if(!form.files.value.match(fileForm)){
            alert("*.mp3 or *.wav 파일만 가능")
            return ;
        }
        if(form.files.files[0].size>maxSize)
        {
            alert("파일 사이즈는 5MB까지 가능");
            return ;
        }

        //업로드 한위치에서의 좌표 가져오기
        fetchCoordinatesAndUse();
        console.log("lat",lat,"lng",lng)


        const username = document.musicForm.username.value;
        const title = document.musicForm.title.value;
        const main_category = document.musicForm.mainCategory.value;

        //선택되어진 sub_category 를 확인

        const sub_categoryEls = document.querySelectorAll('.sub_category.music-cat');
        let sub_category = '';
        sub_categoryEls.forEach(el=>{
            if(!el.classList.contains('hidden'))
                sub_category = el.querySelector('select').value;
        })
        //const description = document.musicForm.description.value;


        music_form_data.append('username',username);
        music_form_data.append('title',title);
        music_form_data.append('mainCategory',main_category);
        music_form_data.append('subCategory',sub_category);
        //music_form_data.append('description',description);
        music_form_data.append('lat',lat);
        music_form_data.append('lng',lng);

        const musicFiles = document.musicForm.files.files[0];
        music_form_data.append("files",musicFiles);


        axios.post('/user/music/add',music_form_data,{ headers: {'Content-Type' :'multipart/form-data' } } )
                .then(res=>{
                    console.log(res);
                    alert("업로드 완료")
                    location.href="/user/album/main";
         })
        .catch(err=>{console.log(err);})
})

