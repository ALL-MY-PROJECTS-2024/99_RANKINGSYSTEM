package com.example.demo.controller;

import com.example.demo.domain.dto.PaymentDto;
import com.example.demo.domain.entity.Cart;
import com.example.demo.domain.entity.Payment;
import com.example.demo.domain.service.CartService;
import com.example.demo.domain.service.PaymentService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/payment")
public class PaymentController {

    // -----------------------------------------------------------
    // 결제 조회 관련 내용은 RESTAPI로 요청해야 한다
    // -----------------------------------------------------------
    // DOCUMENT : https://developers.portone.io/api/rest-v1/payment
    // DOCUMENT - AccessToken사용  https://developers.portone.io/docs/ko/api/rest-api-access-token?v=v1#step-03--%ED%86%A0%ED%81%B0-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
    //
    // 관리자(결제내역확인) : https://classic-admin.portone.io/

    @Autowired
    private CartService cartService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/read")
    public void read(Long[] id_arr, Model model) throws Exception {
        log.info("GET /payment/read");
        for(Long id : id_arr) System.out.println(id);
        List<Cart> list =  cartService.getMyCartItems(id_arr);
        list.forEach(cart->System.out.println(cart));
        model.addAttribute("list",list);

        int totalPrice = 0;
        for(Cart cart : list)
           totalPrice += cart.getAmount() *   Integer.parseInt(cart.getImageBoard().getPrice());
        model.addAttribute("totalPrice",totalPrice);

        List<Long> cart_id_list =new ArrayList<>();
        for(Long cart_id : id_arr)
            cart_id_list.add(cart_id);
        model.addAttribute("cart_id_list", cart_id_list);

    }

    @GetMapping("/add")
    public @ResponseBody void add(PaymentDto dto) throws UnsupportedEncodingException {
        dto.setAddress(  URLDecoder.decode(dto.getAddress(),"UTF-8") );
        dto.setName( URLDecoder.decode(dto.getName(),"UTF-8"));
        List<String> n_card_id = new ArrayList<>();
        for(String cart_id : dto.getCart_id()){
            n_card_id.add( URLDecoder.decode(cart_id,"UTF-8").trim() );
        }
        dto.setCart_id(n_card_id);
        log.info("GET /payment/add dto "+dto);

        boolean isadded =  paymentService.addPayment(dto);
    }

    @GetMapping("/list")
    public void list(Model model) throws Exception {
        log.info("GET /payment/list");
        List<Payment> list = paymentService.getMyPaymentList();
        model.addAttribute("list",list);
    }





    // AccessToken 발급요청
//    @GetMapping("/getAccessToken")
    public String getAccessToken(){
        log.info("GET /payment/getAccessToken....");

        //URL
        String url = "https://api.iamport.kr/users/getToken";

        //Request Header
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        //Request Body
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("imp_key","1546549255738924");
        params.add("imp_secret","Zjy29fdoI6cNNwIZYMrDX4dkLCLvf6HFyFbbVCNwlRD5YzHCEQV4onWbydWFVbT1ID1Zw0Kp6POYsvKg");


        //Hader+Body
        HttpEntity< MultiValueMap<String,String>> entity = new HttpEntity(params,headers);

        //요청
        RestTemplate restTemplate = new RestTemplate();

        //반환값확인
        ResponseEntity<ImportAccessTokenResponse> resp =  restTemplate.exchange(url, HttpMethod.POST,entity,ImportAccessTokenResponse.class);

        System.out.println(resp);
        System.out.println(resp.getBody());
        System.out.println(resp.getBody().getResponse().getAccess_token());
        return resp.getBody().getResponse().getAccess_token();
    }




    //결제 취소 요청
    @GetMapping("/cancel/{imp_uid}/{pay_id}")
    public @ResponseBody void cancel(
            @PathVariable String imp_uid,
            @PathVariable String pay_id
    ){
        log.info("GET /payment/cancel..");
        // access-token 받기
        String access_token = getAccessToken();
        
        //URL
        String url = "https://api.iamport.kr/payments/cancel";

        //Request Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+access_token);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        //Request Body
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("imp_uid",imp_uid);

        //Hader+Body
        HttpEntity< MultiValueMap<String,String>> entity = new HttpEntity(params,headers);

        //요청
        RestTemplate restTemplate = new RestTemplate();

        //반환값확인
        ResponseEntity<String> resp =  restTemplate.exchange(url, HttpMethod.POST,entity,String.class);


        //DB삭제
        paymentService.removePayment(pay_id);

        System.out.println(resp);
        System.out.println(resp.getBody());
    }


}


@Data
class ImportAccessTokenResponse {
    private float code;
    private String message;
    Response response;
}
@Data
class Response {
    private String access_token;
    private float now;
    private float expired_at;
}




