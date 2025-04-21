package com.projectLaas.projectLaas.service;

import com.projectLaas.projectLaas.model.Book;
import com.projectLaas.projectLaas.model.BookCopy;
import com.projectLaas.projectLaas.modelDTO.BookCopyDTO;
import com.projectLaas.projectLaas.repository.BookCopyRepository;
import com.projectLaas.projectLaas.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookCopyService {

    private final BookCopyRepository bookCopyRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BookCopyService(BookCopyRepository bookCopyRepository, BookRepository bookRepository) {
        this.bookCopyRepository = bookCopyRepository;
        this.bookRepository = bookRepository;
    }

    // Get all BookCopies
    public List<BookCopyDTO> getBookCopies() {
        return bookCopyRepository.findAll().stream()
                .filter(bookCopy -> bookCopy.getDeletedDate() == null)
                .map(bookCopy -> new BookCopyDTO(
                        bookCopy.getBook().getId(),
                        bookCopy.getCondition(),
                        bookCopy.getStatus()
                ))
                .collect(Collectors.toList());
    }


    // Get BookCopy by ID
    public Optional<BookCopyDTO> getBookCopyById(Long id) {
        return bookCopyRepository.findById(id)
                .filter(b -> b.getDeletedDate() == null)
                .map(bookCopy -> new BookCopyDTO(
                        bookCopy.getBook().getId(),
                        bookCopy.getCondition(),
                        bookCopy.getStatus()
                ));
    }


    // Add new BookCopy
    public BookCopy addBookCopy(BookCopyDTO newBookCopyDTO) {
        // Fetch the Book entity using bookId from the DTO
        Book book = bookRepository.findById(newBookCopyDTO.getBookId())
                .orElseThrow(() -> new RuntimeException("Book with ID " + newBookCopyDTO.getBookId() + " not found"));

        // Create a new BookCopy entity
        BookCopy bookCopy = new BookCopy();
        bookCopy.setBook(book);
        bookCopy.setCondition(newBookCopyDTO.getCondition());
        bookCopy.setStatus(newBookCopyDTO.getStatus());
        bookCopy.setCreatedDate(new Date());

        // Save the BookCopy entity
        bookCopyRepository.save(bookCopy);
        return bookCopy;
    }

    // Update BookCopy
    public ResponseEntity<Map<String, String>> updateBookCopy(long id, BookCopyDTO bookCopyDTO) {
        // Find the existing BookCopy by ID
        BookCopy updatedBookCopy = bookCopyRepository.findById(id)
                .filter(b -> b.getDeletedDate() == null)
                .orElseThrow(() -> new RuntimeException("BookCopy not found"));

        // Set the new fields from the DTO
        Book book = bookRepository.findById(bookCopyDTO.getBookId())
                .orElseThrow(() -> new RuntimeException("Book with ID " + bookCopyDTO.getBookId() + " not found"));
        updatedBookCopy.setBook(book);
        updatedBookCopy.setCondition(bookCopyDTO.getCondition());
        updatedBookCopy.setStatus(bookCopyDTO.getStatus());
        updatedBookCopy.setLastModifiedDate(new Date());

        // Save the updated BookCopy
        bookCopyRepository.save(updatedBookCopy);

        // Prepare response
        Map<String, String> response = new HashMap<>();
        response.put("message", "BookCopy id: " + updatedBookCopy.getId() + " updated");
        return ResponseEntity.ok(response);
    }

    // Delete BookCopy by ID
    public ResponseEntity<Map<String, String>> deleteBookCopy(long id) {
        if (!bookCopyRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "BookCopy not found"));
        }
        BookCopy toBeDeletedBookCopy = bookCopyRepository.getById(id);
        toBeDeletedBookCopy.setDeletedDate(new Date());
        bookCopyRepository.save(toBeDeletedBookCopy);

        // Prepare response
        Map<String, String> response = new HashMap<>();
        response.put("message", "BookCopy id: " + id + " deleted");
        return ResponseEntity.ok(response);
    }
}
