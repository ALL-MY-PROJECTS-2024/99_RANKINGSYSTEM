
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