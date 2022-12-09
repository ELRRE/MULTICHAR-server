package com.example.multichar.repositories;

import com.example.multichar.Entity.User;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableJpaRepositories
public interface UserRepository extends CrudRepository<User, Long>{
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailOrName(String email, String name);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
    boolean existsByEmailOrName(String email, String name);

}
