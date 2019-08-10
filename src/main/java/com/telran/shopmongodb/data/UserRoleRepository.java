package com.telran.shopmongodb.data;


import com.telran.shopmongodb.data.entity.UserRoleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRoleRepository extends MongoRepository<UserRoleEntity,String> {

}
