package com.telran.shopmongodb.data;

import com.telran.shopmongodb.data.entity.ShoppingCartEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShoppingCartRepository extends MongoRepository<ShoppingCartEntity, String> {
}
