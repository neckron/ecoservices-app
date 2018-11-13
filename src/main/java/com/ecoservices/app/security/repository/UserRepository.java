package com.ecoservices.app.security.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ecoservices.app.model.User;

public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);

}
