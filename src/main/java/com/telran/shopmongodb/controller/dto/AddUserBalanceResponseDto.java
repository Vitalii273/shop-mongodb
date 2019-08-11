package com.telran.shopmongodb.controller.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class AddUserBalanceResponseDto {
    String email;
    Double balance;
}
