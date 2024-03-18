package com.example.demo.domain.service;


import com.example.demo.domain.dto.PaymentDto;
import com.example.demo.domain.entity.Cart;
import com.example.demo.domain.entity.Payment;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.CartRepository;
import com.example.demo.domain.repository.PaymentRepository;
import com.example.demo.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Cart> getCartInfo(Long[] idArr) {

        List<Cart> list =new ArrayList();

        for(Long id : idArr){
            list.add(cartRepository.findById(id).get());
        }
        return list;
    }

    @Transactional(rollbackFor=Exception.class)
    public boolean addPayment(PaymentDto dto) {
        
        //cart_id 의 ] [ 제거
        String [] cart_id =  dto.getCart_id().get(0).split(",");
        cart_id[0] = cart_id[0].replace("[","").trim();
        cart_id[cart_id.length-1] = cart_id[cart_id.length-1].replace("]","").trim();


        Payment payment = new Payment();
        payment.setAddress(dto.getAddress());
        payment.setImp_uid(dto.getImp_uid());
        payment.setName(dto.getName());
        payment.setPaid_amount(dto.getPrice());
        payment.setPay_method(dto.getPay_method());
        payment.setMerchant_uid(dto.getMerchant_uid());
        payment.setStatus(dto.getStatus());
        payment.setRegdate(LocalDateTime.now());


        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user =  userRepository.findById(username).get();
        payment.setUser(user);

        List<Cart> cart_list  = new ArrayList<Cart>();
        List<String> product_id_list = new ArrayList<String>();

        for(String id : cart_id){
            //
            Cart cart = cartRepository.findById(Long.parseLong(id.trim())).get();
            product_id_list.add(cart.getImageBoard().getId()+"");
            //장바구니 에서 제거
            cartRepository.deleteById(Long.parseLong(id.trim()));

        }

        payment.setImageboard_id(product_id_list);

        //결제 저장
        paymentRepository.save(payment);

        return true;
    }



    @Transactional(rollbackFor = Exception.class)
    public void removePayment(String pay_id) {
        Payment payment =   paymentRepository.findById( Long.parseLong(pay_id) ).get();
        paymentRepository.delete(payment);
    }


    @Transactional(rollbackFor = Exception.class)
    public List<Payment> getMyPaymentList() throws Exception {

        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        String username =  authentication.getName();
        return paymentRepository.findByUserUsername(username);
    }
}
