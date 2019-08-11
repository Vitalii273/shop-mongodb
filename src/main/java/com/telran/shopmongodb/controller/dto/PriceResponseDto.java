package com.telran.shopmongodb.controller.dto;

import lombok.*;
import org.bson.types.ObjectId;

import java.math.BigDecimal;

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
