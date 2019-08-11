package com.telran.shopmongodb.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "users")
public class UserEntity {
    @Id
    private String email;
    private String name;
    private String phone;
    private BigDecimal balance;
    private ObjectId shoppingCart;
    private List<ObjectId> orders;
    private UserDetailsEntity userDetailsEntity;

}
