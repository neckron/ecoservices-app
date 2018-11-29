package com.ecoservices.app.security.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ecoservices.app.model.Role;
import com.ecoservices.app.model.User;

public interface RoleRepository extends MongoRepository<Role, String> {

    Role findByRole(String role);

}
