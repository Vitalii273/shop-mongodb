package com.telran.shopmongodb.data.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "shopping_carts")
public class ShoppingCartEntity {
    @Id
    private ObjectId id;
    private LocalDateTime date;
    private List<ObjectId> products;
}
