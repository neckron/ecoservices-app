package com.ecoservices.app.security.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ecoservices.app.model.Role;

public interface RoleRepository extends MongoRepository<Role, String> {

    Role findByRole(String role);
}
