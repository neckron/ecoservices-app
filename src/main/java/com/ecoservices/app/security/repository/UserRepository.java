package com.ecoservices.app.security.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ecoservices.app.model.Role;
import com.ecoservices.app.model.User;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
    Optional<User> findByResetToken(String resetToken);

}
