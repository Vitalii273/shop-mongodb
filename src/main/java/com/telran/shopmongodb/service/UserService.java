package com.telran.shopmongodb.service;

import com.telran.shopmongodb.controller.dto.*;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserDto> addUserInfo(String email, String name, String phone);
    Optional<UserDto> getUserInfo(String email);

    List<ProductDto> getAllProducts();
    List<CategoryDto> getAllCategories();
    List<ProductDto> getProductsByCategory(String categoryId);

    Optional<ShoppingCartDto> addProductToCart(String userEmail, String productId, int count);
    Optional<ShoppingCartDto> removeProductFromCart(String userEmail, String productId, int count);
    Optional<ShoppingCartDto> getShoppingCart(String userEmail);
    boolean clearShoppingCart(String userEmail);

    List<OrderDto> getOrders(String userEmail);

    Optional<OrderDto> checkout(String userEmail);
}
