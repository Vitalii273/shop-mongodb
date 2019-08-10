package com.telran.shopmongodb.controller.dto;

import lombok.*;
import org.bson.types.ObjectId;

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
