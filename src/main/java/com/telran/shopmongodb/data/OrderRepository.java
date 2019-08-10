package com.telran.shopmongodb.data;

import com.telran.shopmongodb.data.entity.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.stream.Stream;

public interface OrderRepository extends MongoRepository<OrderEntity, String> {
    Stream<OrderEntity> findAllByOwner_Email(String userEmail);
}

