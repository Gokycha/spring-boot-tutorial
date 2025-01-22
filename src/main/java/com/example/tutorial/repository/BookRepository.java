package com.example.tutorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tutorial.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    
}
