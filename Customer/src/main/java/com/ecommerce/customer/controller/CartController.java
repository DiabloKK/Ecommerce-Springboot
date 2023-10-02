package com.ecommerce.customer.controller;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.ShoppingCartService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class CartController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ShoppingCartService cartService;

    @GetMapping("/cart")
    public String cart(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Customer customer = customerService.findByUsername(principal.getName());
        ShoppingCart shoppingCart = customer.getShoppingCart();
        if (shoppingCart == null) {
            model.addAttribute("check", "No item in your cart");
        }
        model.addAttribute("shoppingCart", shoppingCart);
        return "cart";
    }

    @PostMapping("/add-to-cart")
    public String addItemToCart(@RequestParam("id") Long productId,
                                @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
                                Principal principal,
                                HttpServletRequest request) {

        if(principal == null){
            return "redirect:/login";
        }

        Product product = productService.getProductById(productId);
        Customer customer = customerService.findByUsername(principal.getName());

        ShoppingCart cart = cartService.addItemToCart(product, quantity, customer);

        return "redirect:" + request.getHeader("Referer");
    }

}
