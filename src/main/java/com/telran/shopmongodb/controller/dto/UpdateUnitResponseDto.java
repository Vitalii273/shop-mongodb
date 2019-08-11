package com.telran.shopmongodb.controller.dto;

import lombok.*;

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
