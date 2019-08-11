package com.telran.shopmongodb.controller.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String email;
    private String name;
    private String phone;
    private Double balance;
}
