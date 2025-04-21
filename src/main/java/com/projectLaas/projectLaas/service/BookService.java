package com.projectLaas.projectLaas.service;

import com.projectLaas.projectLaas.model.Book;
import com.projectLaas.projectLaas.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooks() {
        List<Book> books = bookRepository.findAll()
                .stream()
                .filter(book -> book.getDeletedDate() == null)
                .collect(Collectors.toList());

        return books;
    }

    public Optional<Book> getBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.filter(b -> b.getDeletedDate() == null);
    }

    public Book addBook(Book newBook) {
        Book book = new Book();
        book.setTitle(newBook.getTitle());
        book.setAuthor(newBook.getAuthor());
        book.setDescription(newBook.getDescription());
        book.setIsbn10(newBook.getIsbn10());
        book.setIsbn13(newBook.getIsbn13());
        book.setImgUrl(newBook.getImgUrl());

        if(newBook.getEbookUrl() != null) {
            book.setEbookUrl(newBook.getEbookUrl());
        }
        book.setCreatedDate(new Date());

        bookRepository.save(book);
        return book;
    }

    public ResponseEntity<Map<String, String>> updateBook(long id, Book book) {
        Book updatedBook = bookRepository.findById(id)
                .filter(b -> b.getDeletedDate() == null)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        updatedBook.setTitle(book.getTitle());
        updatedBook.setAuthor(book.getAuthor());
        updatedBook.setDescription(book.getDescription());
        updatedBook.setIsbn10(book.getIsbn10());
        updatedBook.setIsbn13(book.getIsbn13());
        updatedBook.setImgUrl(book.getImgUrl());

        if(book.getEbookUrl() != null) {
            updatedBook.setEbookUrl(book.getEbookUrl());
        }
        updatedBook.setLastModifiedDate(new Date());
        bookRepository.save(updatedBook);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Book id: " + updatedBook.getId() + " updated");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, String>> deleteBook(long id) {
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Book not found"));
        }
        Book toBeDeletedBook = bookRepository.getById(id);
        toBeDeletedBook.setDeletedDate(new Date());
        bookRepository.save(toBeDeletedBook);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Book id: " + id + " deleted");
        return ResponseEntity.ok(response);
    }
}