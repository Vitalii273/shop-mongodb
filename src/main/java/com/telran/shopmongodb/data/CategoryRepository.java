package com.telran.shopmongodb.data;

import com.telran.shopmongodb.data.entity.CategoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<CategoryEntity,String> {
}
