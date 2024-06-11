//이미지 랭킹 추가
const addRankingEls = document.querySelectorAll(".addRanking");
addRankingEls.forEach(addRankingEl=>{

    addRankingEl.addEventListener("click",function(e){
        console.log("addRanking ",e.target);

        if(confirm("이미지를 RANKING BOARD에 등록하시겠습니까?")){
                const fileid = this.getAttribute('data-fileid');
                console.log("fileid",fileid);

                axios.get("/imageRanking/add?fileid="+fileid)
                .then(resp=>{
                    console.log(resp.data);
                    if(confirm(resp.data+" 랭킹페이지로 이동할까요?"))
                        location.href="/imageRanking/list";

                })
                .catch(err=>{console.log(err)})
        }
    })


})


//이미지 삭제
const deleteRankingEls = document.querySelectorAll(".deleteRanking");
deleteRankingEls.forEach(deleteRankingEl=>{
    deleteRankingEl.addEventListener("click",function(){
        console.log("deleteRanking")

        const fileid = this.getAttribute('data-fileid');
        if(confirm("해당 앨범이미지 삭제시 등록된 랭킹에서도 함께 삭제 됩니다.\n진행하시겠습니까?"))
        {
            axios.delete("/user/album/delete?fileid=" +fileid )
            .then(resp=>{
                console.log(resp.data);
                if(confirm(resp.data)){
                    location.href="/user/album/main";
                }
            })
            .catch(err=>{
                console.log(err)
                alert(err.response.data);
                }
            )

        }
        else{
            alert("취소 되었습니다.");
        }

    })
})

//카테고리 변경
const changeCategoryEls = document.querySelectorAll(".changeCategory");
changeCategoryEls.forEach(changeCategory=>{
    changeCategory.addEventListener("click",function(){
        console.log("changeCategory")
    })
})