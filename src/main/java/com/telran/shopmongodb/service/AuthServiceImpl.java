package com.telran.shopmongodb.service;

import com.telran.shopmongodb.data.UserDetailsRepository;
import com.telran.shopmongodb.data.entity.UserDetailsEntity;
import com.telran.shopmongodb.data.entity.UserRoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    @Autowired
    UserDetailsRepository repository;
    @Autowired
    PasswordEncoder encoder;
    @Override
    public void registration(String email, String password) {
        if(repository.existsById(email)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exist");
        }

        UserDetailsEntity entity = new UserDetailsEntity();
        entity.setEmail(email);
        entity.setPassword(encoder.encode(password));
        entity.setRoles(
                List.of(
                        UserRoleEntity.builder()
                                .role("ROLE_USER")
                                .build()
                )
        );
        repository.save(entity);
    }
}
