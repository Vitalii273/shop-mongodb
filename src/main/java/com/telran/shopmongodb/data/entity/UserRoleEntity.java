package com.telran.shopmongodb.data.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Document(collection = "roles")
public class UserRoleEntity {
    @Id
    private String role;
    private List<ObjectId> users_details;

}
