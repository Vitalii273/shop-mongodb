package com.telran.shopmongodb.controller.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UserStatisticDto {
    private String userEmail;
    private List<ProductOrderDto> products;
    private int totalProductsCount;
    private Double totalAmount;
}
