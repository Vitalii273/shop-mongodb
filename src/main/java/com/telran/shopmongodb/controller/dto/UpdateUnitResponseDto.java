package com.telran.shopmongodb.controller.dto;

import lombok.*;
import org.bson.types.ObjectId;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UpdateUnitResponseDto {
    private String id;
    private String name;
}
