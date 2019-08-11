package com.telran.shopmongodb.controller.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CategoryDto {
    private String id;
    private String name;
}
