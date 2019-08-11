package com.telran.shopmongodb.service;

import com.telran.shopmongodb.controller.dto.AddUserBalanceResponseDto;

import java.util.Optional;

public interface AdminService {
    String addCategory(String categoryName);
    String addProduct(String productName, Double price, String categoryId);
    boolean removeProduct(String productId);
//    boolean removeCategory(String categoryId);
    boolean updateCategory(String categoryId, String categoryName);
    boolean changeProductPrice(String productId, Double price);
    Optional<AddUserBalanceResponseDto> addBalance(String userEmail, Double balance);
}
