//x 버튼 클릭시 장바구니  items 삭제
const cart_delete_btn_els = document.querySelectorAll('.cart_delete_btn');

cart_delete_btn_els.forEach(el=>{

    el.addEventListener('click',function(){
        const cart_id =  this.getAttribute('data-id');
         console.log('cart_id',cart_id);
         axios.delete(`/cart/delete?cart_id=${cart_id}`)
         .then(
            resp=>{
                console.log(resp)
                alert("장바구니에서 항목을 삭제 했습니다.")
                location.reload();
            }
         )
         .catch(err=>{console.log(err)})
    })
})



// 체크박스 요소 참조
let cart_list_arr = [];

var checkboxEls = document.querySelectorAll(".item_checkbox");

checkboxEls.forEach(checkbox=>{
    // 체크박스의 상태가 변경될 때 발생하는 이벤트 핸들러
    checkbox.addEventListener("change", function() {
            // 체크박스의 체크 상태 확인
            if (checkbox.checked) {
                console.log("체크됨");
                const item_board_block_El =  checkbox.parentNode.parentNode;
                console.log(item_board_block_El);
                const price= item_board_block_El.querySelector(".price")

                const item_body_block_El = item_board_block_El.parentNode;

                const amount= item_body_block_El.querySelector(".amount")
                console.log('amount',amount);
                console.log('price',price.innerHTML);
                const totalPriceEl = document.querySelector('.total_price');
                totalPriceEl.innerHTML=(Number(price.innerHTML) * Number(amount.innerHTML)) +Number(totalPriceEl.innerHTML);

                //배송비


               // const delivery_price = document.querySelector('.delivery_price');

                //전체 주문금액
                const total_delivery_price = document.querySelector('.total_delivery_price');
                total_delivery_price.innerHTML = Number(totalPriceEl.innerHTML);

                //cart 에 넣기
                const cart_id =  checkbox.getAttribute('data-cartid');

                cart_list_arr.push(cart_id);
                console.log(cart_list_arr);


            } else {
                console.log("체크 해제됨");
                const item_board_block_El =  checkbox.parentNode.parentNode;
                console.log(item_board_block_El);
                const price= item_board_block_El.querySelector(".price")

                const item_body_block_El = item_board_block_El.parentNode;

                const amount= item_body_block_El.querySelector(".amount")
                console.log('amount',amount);
                console.log('price',price.innerHTML);
                const totalPriceEl = document.querySelector('.total_price');
                totalPriceEl.innerHTML=Number(totalPriceEl.innerHTML) - (Number(price.innerHTML) * Number(amount.innerHTML)) ;


                //const delivery_price = document.querySelector('.delivery_price');
                //전체 주문금액
                const total_delivery_price = document.querySelector('.total_delivery_price');
                total_delivery_price.innerHTML = Number(total_delivery_price.innerHTML) - (Number(price.innerHTML) * Number(amount.innerHTML));


                //카트 뺴기
                cart_list_arr.pop();
                console.log(cart_list_arr);

            }
        });

})


const req_pay_btn = document.querySelector('.req_pay_btn');
req_pay_btn.addEventListener('click',function(){

    location.href="/payment/read?id_arr="+cart_list_arr;

})











