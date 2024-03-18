package com.example.demo.domain.repository;

import com.example.demo.domain.entity.Cart;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Test
    public void t1(){
          List<Cart> list =   cartRepository.findByUserUsername("user1@naver.com");

          list.forEach(item->System.out.println(item));


    }

}