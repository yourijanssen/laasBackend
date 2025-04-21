package com.projectLaas.projectLaas.controller;

import com.projectLaas.projectLaas.model.BookCopy;
import com.projectLaas.projectLaas.modelDTO.BookCopyDTO;
import com.projectLaas.projectLaas.service.BookCopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookCopyController {

    private final BookCopyService bookCopyService;

    @Autowired
    public BookCopyController(BookCopyService bookCopyService) {
        this.bookCopyService = bookCopyService;
    }

    @GetMapping("/getBookCopies")
    public ResponseEntity<?> getBooks() {
        try {
            return new ResponseEntity<>(bookCopyService.getBookCopies(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred during getting all book copies", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBookCopyById/{id}")
    public ResponseEntity<?> getBookCopyById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(bookCopyService.getBookCopyById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred during getting book copy by id", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/addBookCopy")
    public ResponseEntity<?> addBookCopy(@RequestBody BookCopyDTO newBookCopyDTO) {
        try {
            return new ResponseEntity<>(bookCopyService.addBookCopy(newBookCopyDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred during adding Book Copy", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateBookCopy/{id}")
    public ResponseEntity<?> updateBookCopy(@PathVariable long id, @RequestBody BookCopyDTO bookCopyDTO) {
        try {
            return new ResponseEntity<>(bookCopyService.updateBookCopy(id, bookCopyDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred during updating book copy", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteBookCopy/{id}")
    public ResponseEntity<?> deleteBookCopy(@PathVariable long id) {
        try {
            return new ResponseEntity<>(bookCopyService.deleteBookCopy(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred during deleting book copy", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

