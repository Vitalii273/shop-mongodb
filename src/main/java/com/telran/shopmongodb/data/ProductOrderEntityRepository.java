package com.telran.shopmongodb.data;

import com.telran.shopmongodb.data.entity.ProductOrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductOrderEntityRepository extends MongoRepository<ProductOrderEntity,String> {

}
