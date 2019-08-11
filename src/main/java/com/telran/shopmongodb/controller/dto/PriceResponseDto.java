package com.telran.shopmongodb.controller.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PriceResponseDto {
    String id;
    Double price;
}
