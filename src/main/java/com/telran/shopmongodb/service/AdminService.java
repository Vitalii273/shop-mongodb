package com.telran.shopmongodb.service;

import com.mongodb.client.MongoCollection;
import com.telran.shopmongodb.data.entity.OrderEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public interface AdminService {
    String addCategory(String categoryName);
    String addProduct(String productName, Double price, String categoryId);
    boolean removeProduct(String productId);
//    boolean removeCategory(String categoryId);
    boolean updateCategory(String categoryId, String categoryName);
    boolean changeProductPrice(String productId, Double price);
    boolean addBalance(String userEmail, Double balance);
}
