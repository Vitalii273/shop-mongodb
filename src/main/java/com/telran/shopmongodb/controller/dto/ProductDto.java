package com.telran.shopmongodb.controller.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ProductDto {
    private String id;
    private String name;
    private Double price;
    private CategoryDto category;
}
