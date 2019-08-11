package com.telran.shopmongodb.controller;

import com.telran.shopmongodb.controller.dto.*;
import com.telran.shopmongodb.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService service;

    @PostMapping("user")
    @ApiOperation(value = "Add new profile")
    public UserDto addUserInfo(@RequestBody UserDto user, Principal principal) {
        try {
            return service.addUserInfo(principal.getName(), user.getName(), user.getPhone())
                    .orElseThrow();
        } catch (Throwable throwable) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User profile with email [" + principal.getName() + "] or phone [" + user.getPhone() + "] already exist");
        }
    }

    @GetMapping("user")
    @ApiOperation(value = "Get user info")
    public UserDto getUserInfo(Principal principal) {
        return service.getUserInfo(principal.getName())
                .orElseThrow();
    }

    @GetMapping("products")
    @ApiOperation(value = "Get all products")
    public List<ProductDto> getAllProducts() {
        return service.getAllProducts();
    }

    @GetMapping("categories")
    @ApiOperation(value = "Get all categories")
    public List<CategoryDto> getAllCategories() {
        return service.getAllCategories();
    }

    @GetMapping("products/{categoryName}")
    @ApiOperation(value = "Get product by category Id")
    public List<ProductDto> getProductByCategory(@PathVariable("categoryName") String categoryName) {
        return service.getProductsByCategory(categoryName);
    }

    @PostMapping("cart")
    @ApiOperation(value = "Add product to cart")
    public ShoppingCartDto addProductToCart(Principal principal, @RequestBody AddProductDto dto) {
        return service.addProductToCart(principal.getName(), dto.getProductId(), dto.getCount())
                .orElseThrow();
    }

    @GetMapping("cart")
    @ApiOperation(value = "Get shopping cart")
    public ShoppingCartDto getShoppingCart(Principal principal) {
        return service.getShoppingCart(principal.getName())
                .orElseThrow();
    }

    @DeleteMapping("cart/{productId}/{count}")
    @ApiOperation(value = "Remove product from cart + count")
    public ShoppingCartDto removeProductFromCart(Principal principal,
                                                 @PathVariable("productId") String productId,
                                                 @PathVariable("count") int count) {
        return service.removeProductFromCart(principal.getName(), productId, count)
                .orElseThrow();
    }

    @DeleteMapping("cart/all")
    @ApiOperation(value = "Shopping cart clear")
    public void clearShoppingCart(Principal principal) {
        service.clearShoppingCart(principal.getName());
    }

    @GetMapping("orders")
    @ApiOperation(value = "Get order by email")
    public List<OrderDto> getAllOrdersByEmail(Principal principal) {
        return service.getOrders(principal.getName());
    }


    @GetMapping("checkout")
    @ApiOperation(value = "Checkout")
    public OrderDto checkout(Principal principal) {
        return service.checkout(principal.getName())
                .orElseThrow();
    }
}
