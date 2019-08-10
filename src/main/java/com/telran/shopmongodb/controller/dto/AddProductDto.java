package com.telran.shopmongodb.controller.dto;

import lombok.*;
import org.bson.types.ObjectId;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class AddProductDto {
    private String productId;
    private int count;
}
