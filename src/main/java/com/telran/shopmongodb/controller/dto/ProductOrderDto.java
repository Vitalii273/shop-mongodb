package com.telran.shopmongodb.controller.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ProductOrderDto {
    private ProductDto product;
    private int count;
}
