    const pay_phone_btn = document.querySelector('.pay_phone_btn');
     pay_phone_btn.addEventListener('click',function(){

       const productnameEls =  document.querySelectorAll('.productname');
       const productname=productnameEls[0].innerHTML+ ' 외 '+(productnameEls.length-1)+" 건";
       const totalPrice=document.querySelector('.totalPrice').innerHTML;
       const phone = document.querySelector('.phone').innerHTML;
       const address = document.querySelector(".address").innerHTML;
       const cart_id_list = document.querySelector('.cart_id_list').value;

        console.log(productname);

         IMP.init("imp87380722");

         IMP.request_pay(
             {
                 pg: "danal",
                 pay_method: "phone",
                 merchant_uid: "merchant_"+new Date().getTime() ,
                 name: productname,
                 amount: totalPrice,
                 buyer_tel: phone,
             }
         ,function(resp){
            console.log(resp);

            const params = { params : {
                imp_uid : resp.imp_uid,
                merchant_uid : resp.merchant_uid,
                pay_method : resp.pay_method,
                name : encodeURIComponent(productname),
                price : resp.paid_amount,
                status :resp.status,
                address : encodeURIComponent(address),
                cart_id : encodeURIComponent(cart_id_list)
            }}

            axios.get("/payment/add",params)
            .then(resp=>{
                    console.log(resp);
                    alert("결제가 완료되었습니다. 결제 확인페이지로 이동합니다.");
                    location.href="/payment/list";
                })
            .catch(err=>{console.log(err);})



         });

     })


