package com.telran.shopmongodb.data.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class OrderStatistic {
    private String name;
    private Set<String> productName;
    private double totalPrice;
    private int totalCount;
}
