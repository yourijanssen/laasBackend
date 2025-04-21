package com.projectLaas.projectLaas;

import com.projectLaas.projectLaas.model.Book;
import com.projectLaas.projectLaas.repository.BookRepository;
import com.projectLaas.projectLaas.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setDescription("Test Description");
        book.setIsbn10("1234567890");
        book.setIsbn13("123-1234567890");
        book.setImgUrl("http://test.com/image.jpg");
    }

    @Test
    void testGetBooks() {
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetBookByIdWhenBookExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.getBookById(1L);

        assertTrue(result.isPresent());
        assertEquals(book.getTitle(), result.get().getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBookByIdWhenBookDoesNotExist() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.getBookById(1L);

        assertFalse(result.isPresent());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testAddBook() {
        // Create a new book object for testing
        Book newBook = new Book();
        newBook.setTitle("New Book");
        newBook.setAuthor("New Author");
        newBook.setDescription("New Description");
        newBook.setIsbn10("9876543210");
        newBook.setIsbn13("987-9876543210");
        newBook.setImgUrl("http://new.com/image.jpg");

        // Mock the behavior of bookRepository.save to return the book with "New Book" title
        when(bookRepository.save(any(Book.class))).thenReturn(newBook);

        // Call the service method
        Book result = bookService.addBook(newBook);

        // Assert that the result book title is "New Book"
        assertNotNull(result);
        assertEquals("New Book", result.getTitle());  // Expecting "New Book"
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testUpdateBook() {
        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Book");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setDescription("Updated Description");
        updatedBook.setIsbn10("1122334455");
        updatedBook.setIsbn13("112-1122334455");
        updatedBook.setImgUrl("http://updated.com/image.jpg");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        ResponseEntity<Map<String, String>> response = bookService.updateBook(1L, updatedBook);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("message"));
        assertTrue(response.getBody().get("message").contains("updated"));
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testUpdateBookWhenBookNotFound() {
        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Book");

        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> bookService.updateBook(1L, updatedBook));

        assertEquals("Book not found", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteBook() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.getById(1L)).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        ResponseEntity<Map<String, String>> response = bookService.deleteBook(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("message"));
        assertTrue(response.getBody().get("message").contains("deleted"));
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testDeleteBookWhenBookNotFound() {
        when(bookRepository.existsById(1L)).thenReturn(false);

        ResponseEntity<Map<String, String>> response = bookService.deleteBook(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Book not found", response.getBody().get("error"));
        verify(bookRepository, times(1)).existsById(1L);
    }
}
