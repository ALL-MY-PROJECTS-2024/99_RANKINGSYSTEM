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
                    alert(resp.data);

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
    })
})

//카테고리 변경
const changeCategoryEls = document.querySelectorAll(".changeCategory");
changeCategoryEls.forEach(changeCategory=>{
    changeCategory.addEventListener("click",function(){
        console.log("changeCategory")
    })
})