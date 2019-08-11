package com.telran.shopmongodb.service;

import com.telran.shopmongodb.controller.dto.*;
import com.telran.shopmongodb.data.ProductOrderEntityRepository;
import com.telran.shopmongodb.data.entity.*;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Component
public class Mapper {
    @Autowired
    ProductOrderEntityRepository productOrderEntityRepository;

    public  UserDto map(UserEntity entity) {
        return UserDto.builder()
                .email(entity.getEmail())
                .name(entity.getName())
                .phone(entity.getPhone())
                .balance(entity.getBalance())
                .build();
    }

    public  ProductDto map(ProductEntity entity) {
        return ProductDto.builder()
                .category(map(entity.getCategory()))
                .name(entity.getName())
                .id(entity.getId().toHexString())
                .price(entity.getPrice())
                .build();
    }

    public  CategoryDto map(CategoryEntity entity) {
        return CategoryDto.builder()
                .id(entity.getId().toHexString())
                .name(entity.getName())
                .build();
    }

    public  ShoppingCartDto map(ShoppingCartEntity entity) {
        return ShoppingCartDto.builder()
                .products(entity.getProducts()
                        .stream()
                        .map(ObjectId::toHexString)
                        .map(productOrderEntityRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(this::map)
                        .collect(toList())
                )
                .build();
    }

    private  ProductOrderDto map(ProductOrderEntity entity) {
        return ProductOrderDto.builder()
                .product(ProductDto.builder()
                        .id(entity.getProductId().toHexString())
                        .name(entity.getName())
                        .price(entity.getPrice())
                        .category(map(entity.getCategory()))
                        .build())
                .count(entity.getCount())
                .build();
    }

    public  OrderDto map(OrderEntity entity) {
        return OrderDto.builder()
                .date(LocalDateTime.now())
                .id(entity.getId().toHexString())
                .products(entity.getProducts()
                        .stream()
                        .map(ObjectId::toHexString)
                        .map(productOrderEntityRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(this::map)
                        .collect(toList()))
                .status(entity.getStatus().name())
                .build();
    }
}
