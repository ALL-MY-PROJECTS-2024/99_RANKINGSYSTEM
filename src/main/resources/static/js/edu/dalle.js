const modalBtn = document.querySelector('.modal-btn');
const addLecture = document.querySelector('.add-lecture');
const addModal = document.querySelector('.add-modal-btn');

//--------------------------------------
//강의추가
//--------------------------------------
//관리자 모달창 활성화
addLecture.addEventListener('click',function(){
    modalBtn.click();
})

//관리자가 등록
addModal.addEventListener('click',function(){

    const type = document.addLectureForm.type.value;

    if(type=='file')
    {
        console.log('file');
        var file = document.addLectureForm.file.files[0];
        if (file) {
            var fileType = file.type.split('/')[0]; // 파일 타입 확인 ('video', 'image', 'text' 등)
            var fileSize = file.size; // 파일 사이즈 확인 (단위: byte)

            // 동영상 파일 체크 및 파일 사이즈 체크 사이즈 50MB 이하
            if (fileType === 'video' && fileSize <= 50 * 1024 * 1024) {
                // 유효한 동영상 파일이고 사이즈가 50MB 이하인 경우// 여기에 추가적인 로직을 추가할 수 있습니다.
                console.log('유효한 동영상 파일입니다.');
            } else {
                // 유효하지 않은 파일일 경우
                this.value = ''; // 파일 선택 창 비우기
                alert('유효하지 않은 파일입니다. 동영상 파일이고 사이즈가 50MB 이하인 파일을 선택해주세요.');
                return ;
            }
        }
    }
    else //link
    {
        console.log('link');
    }
    document.addLectureForm.submit();

})

//Type vs link
document.addLectureForm.type.addEventListener('change',function(){
    const selectOption = this.value;

    switch(selectOption){
        case "file" :
            document.addLectureForm.link.disabled=true;
            document.addLectureForm.file.disabled=false;
            break;
        case "link" :
            document.addLectureForm.link.disabled=false;
            document.addLectureForm.file.disabled=true;
            break;
    }

})


//--------------------------------------
//강의삭제
//--------------------------------------
const deleteModalBtn = document.querySelector('.modal-btn-delete');
const deleteLecture = document.querySelector('.delete-lecture');
const deleteModalEls = document.querySelectorAll('.delete-modal-btn');

//관리자 모달창 활성화
deleteLecture.addEventListener('click',function(){
    deleteModalBtn.click();
})


//관리자가 삭제
deleteModalEls.forEach(el=>{
    el.addEventListener('click',function(){
        console.log('clicked');

        axios.delete('url')
            .then(res=>{console.log(resp)})
            .catch(err=>{console.log(err)});
    });

})