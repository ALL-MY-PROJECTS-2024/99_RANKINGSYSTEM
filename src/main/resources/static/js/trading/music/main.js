const remittanceBtn=document.querySelectorAll('.remittanceBtn');
remittanceBtn.forEach(btn=>{

    btn.addEventListener('click',function(){
        const tradingid = btn.getAttribute('data-tradingid');
        axios.get(`/payment/remittance?tradingid=${tradingid}`)
        .then(resp=>{
            console.log(resp);
            alert('송금완료!');
            location.href="/trading/music/main";
        })
        .catch(err=>{console.log(err);})
    })
})

