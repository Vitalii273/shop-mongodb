package com.telran.shopmongodb.controller.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ShoppingCartDto {
    private List<ProductOrderDto> products;
}
