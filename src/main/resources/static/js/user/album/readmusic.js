//이미지 랭킹 추가
const addRankingEls = document.querySelectorAll(".addRanking");
addRankingEls.forEach(addRankingEl=>{

    addRankingEl.addEventListener("click",function(e){
        console.log("addRanking ",e.target);

        if(confirm("음악을 RANKING BOARD에 등록하시겠습니까?")){
                const fileid = this.getAttribute('data-fileid');
                console.log("fileid",fileid);

                axios.get("/musicRanking/add?fileid="+fileid)
                .then(resp=>{
                    console.log(resp.data);
                    if(confirm(resp.data+" 랭킹페이지로 이동할까요?"))
                        location.href="/musicRanking/list";

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
        if(confirm("해당 음악파일 삭제시 등록된 랭킹에서도 함께 삭제 됩니다.\n진행하시겠습니까?"))
        {
            axios.delete("/user/album/musicdelete?fileid=" +fileid )
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

//경매 요청
const requestAuctionEls = document.querySelectorAll(".requestAuction");
requestAuctionEls.forEach(requestAuction=>{
    requestAuction.addEventListener("click",function(){

        const fileid = requestAuction.getAttribute("data-fileid");
        console.log("requestAuction",fileid)

        const startPrice = prompt("시작가격을 입력하세요 : ");
        axios.get('/trading/music/req?fildid='+fileid+"&startPrice="+startPrice)
        .then(resp=>{
            console.log(resp);
            alert(resp.data);
        })
        .catch(err=>{
            console.log(err.response.data);
            alert(err.response.data+"");
        })
    })
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