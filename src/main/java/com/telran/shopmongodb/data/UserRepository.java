package com.telran.shopmongodb.data;

import com.telran.shopmongodb.data.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity,String> {
}
