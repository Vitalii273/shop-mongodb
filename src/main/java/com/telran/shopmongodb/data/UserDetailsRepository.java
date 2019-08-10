package com.telran.shopmongodb.data;

import com.telran.shopmongodb.data.entity.UserDetailsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDetailsRepository extends MongoRepository<UserDetailsEntity,String> {
}
