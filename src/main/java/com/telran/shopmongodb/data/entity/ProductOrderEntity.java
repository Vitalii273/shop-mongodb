package com.telran.shopmongodb.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="product_order")
public class ProductOrderEntity {
    @Id
    private ObjectId id;
    private ObjectId productId;
    private String name;
    private int count;
    private Double price;
    private CategoryEntity category;
    private ObjectId shoppingCart;
    private ObjectId order;
}
