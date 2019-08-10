package com.telran.shopmongodb.data;

import com.telran.shopmongodb.data.entity.ProductEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.stream.Stream;

public interface ProductRepository extends MongoRepository<ProductEntity,String> {
    Stream<ProductEntity> findAllByCategory_Id(String category);
    ProductEntity findProductEntityById(String productId);

}
