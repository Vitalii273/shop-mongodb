package com.telran.shopmongodb.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(collection = "user_details")
public class UserDetailsEntity {
    @Id
    private String email;
    private String password;
    private List<UserRoleEntity> roles;

    private String owner_email;
}
