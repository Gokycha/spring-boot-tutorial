package com.example.tutorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tutorial.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByNameAndHashPassword(String name, String hashPassword);
}