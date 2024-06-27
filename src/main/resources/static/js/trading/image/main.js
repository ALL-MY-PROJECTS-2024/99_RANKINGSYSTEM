
const userAccountBtn=document.querySelector('.userAccountBtn');
const remittanceBtn=document.querySelectorAll('.remittanceBtn');
remittanceBtn.forEach(btn=>{
    btn.addEventListener('click',function(){

        const tradingid = btn.getAttribute('data-tradingid');

        axios.get(`/payment/image/getSellerAccount?tradingid=${tradingid}`)
        .then(resp=>{
            console.log(resp);
            const bankname =document.querySelector('.bankname');
            const account =document.querySelector('.account');
            const trading_id =document.querySelector('.trading_id');

            if(resp.data.bankname==null ||resp.data.bankname=='undefined'){
                alert("판매자 계좌등록이 완료되지 않았습니다.");
                return ;
            }else{
                bankname.innerHTML=resp.data.bankname;
                account.innerHTML=resp.data.account;
                trading_id.innerHTML=tradingid;
                userAccountBtn.click();
            }

        })
        .catch(err=>{console.log(err);})


    })
})

const sendMoneyBtn=document.querySelector('.sendMoneyBtn');
sendMoneyBtn.addEventListener('click',function(){
        const tradingid = document.querySelector('.trading_id').innerHTML;
        axios.get(`/payment/remittance/image?tradingid=${tradingid}`)
        .then(resp=>{
            console.log(resp);
            alert('송금완료!');
            location.href="/trading/image/main";
        })
        .catch(err=>{console.log(err);})
})
