package com.projectLaas.projectLaas.controller;

import com.projectLaas.projectLaas.model.Book;
import com.projectLaas.projectLaas.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/getBooks")
    public ResponseEntity<?>  getBooks() {
        try{
            return new ResponseEntity<>(bookService.getBooks(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred during getting all books", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBookById/{id}")
    public ResponseEntity<?>  getBookById(@PathVariable Long id) {
        try{
            return new ResponseEntity<>(bookService.getBookById(id), HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>("An error occurred during getting book by id", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path="/addBook")
    public ResponseEntity<?> addBook(@RequestBody Book newBook) {
        try {
            return new ResponseEntity<>(bookService.addBook(newBook), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred during adding Book", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateBook/{id}")
    public ResponseEntity<?> updateBook(@PathVariable long id, @RequestBody Book book) {
        try{
            return new ResponseEntity<>(bookService.updateBook(id, book), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred during updating Book", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable long id) {
        try{
            return new ResponseEntity<>(bookService.deleteBook(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred during deleting book", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}