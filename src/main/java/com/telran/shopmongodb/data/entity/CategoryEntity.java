package com.telran.shopmongodb.data.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@ToString(exclude = "products")
@Document(collection = "categories")
public class CategoryEntity {
    @Id
    private ObjectId id;
    private String name;
    private List<ProductEntity> products;
}
