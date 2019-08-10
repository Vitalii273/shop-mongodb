package com.telran.shopmongodb.controller.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OrderDto {
    private String id;
    private LocalDateTime date;
    private String status;
    private List<ProductOrderDto> products;
}
