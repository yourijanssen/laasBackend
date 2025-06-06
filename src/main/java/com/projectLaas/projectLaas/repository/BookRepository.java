package com.projectLaas.projectLaas.repository;

import com.projectLaas.projectLaas.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}