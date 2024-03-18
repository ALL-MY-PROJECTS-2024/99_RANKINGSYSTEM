package com.example.demo.controller;


import com.example.demo.domain.dto.CartDto;
import com.example.demo.domain.entity.Cart;
import com.example.demo.domain.service.CartService;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/read")
    public void read(Model model) throws Exception {
        log.info("GET /cart/read..");
        List<Cart> list =  cartService.getMyCartItems();
        list.forEach(item->System.out.println(item));

        model.addAttribute("list",list);

    }

    @GetMapping("/add")
    public @ResponseBody void add(CartDto dto){
        log.info("GET /cart/add... dto " + dto);

        boolean isok  = cartService.addCart(dto);
    }

    @DeleteMapping("/delete")
    public @ResponseBody void delete(Long cart_id) throws Exception {
        log.info("GET /cart/delete id : " + cart_id);
        boolean isdeleted = cartService.deleteCart(cart_id);

    }

}
