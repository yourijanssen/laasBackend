package com.projectLaas.projectLaas;

import com.projectLaas.projectLaas.model.Book;
import com.projectLaas.projectLaas.model.BookCopy;
import com.projectLaas.projectLaas.modelDTO.BookCopyDTO;
import com.projectLaas.projectLaas.repository.BookCopyRepository;
import com.projectLaas.projectLaas.repository.BookRepository;
import com.projectLaas.projectLaas.service.BookCopyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookCopyServiceTest {

    @Mock
    private BookCopyRepository bookCopyRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookCopyService bookCopyService;

    private Book book;
    private BookCopy bookCopy;
    private BookCopyDTO bookCopyDTO;

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

        bookCopy = new BookCopy();
        bookCopy.setId(1L);
        bookCopy.setBook(book);
        bookCopy.setCondition("Good");
        bookCopy.setStatus("Available");

        bookCopyDTO = new BookCopyDTO(1L, "Good", "Available");
    }

    @Test
    void testGetAllBookCopies() {
        List<BookCopy> bookCopies = Arrays.asList(bookCopy);
        when(bookCopyRepository.findAll()).thenReturn(bookCopies);

        List<BookCopyDTO> result = bookCopyService.getBookCopies();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookCopyRepository, times(1)).findAll();
    }

    @Test
    void testGetBookCopyByIdExists() {
        when(bookCopyRepository.findById(1L)).thenReturn(Optional.of(bookCopy));

        Optional<BookCopyDTO> result = bookCopyService.getBookCopyById(1L);

        assertTrue(result.isPresent());
        assertEquals("Good", result.get().getCondition());
        verify(bookCopyRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBookCopyByIdNotExists() {
        when(bookCopyRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<BookCopyDTO> result = bookCopyService.getBookCopyById(1L);

        assertFalse(result.isPresent());
        verify(bookCopyRepository, times(1)).findById(1L);
    }

    @Test
    void testAddBookCopyUsingDTO() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookCopyRepository.save(any(BookCopy.class))).thenReturn(bookCopy);

        BookCopy result = bookCopyService.addBookCopy(bookCopyDTO);

        assertNotNull(result);
        assertEquals("Good", result.getCondition());
        assertEquals("Available", result.getStatus());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookCopyRepository, times(1)).save(any(BookCopy.class));
    }

    @Test
    void testAddBookCopyWithInvalidBookId() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                bookCopyService.addBookCopy(bookCopyDTO)
        );

        assertEquals("Book with ID 1 not found", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookCopyRepository, never()).save(any(BookCopy.class));
    }

    @Test
    void testUpdateBookCopy() {
        BookCopyDTO updatedDTO = new BookCopyDTO(1L, "Excellent", "Checked Out");

        when(bookCopyRepository.findById(1L)).thenReturn(Optional.of(bookCopy));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookCopyRepository.save(any(BookCopy.class))).thenReturn(bookCopy);

        ResponseEntity<Map<String, String>> response = bookCopyService.updateBookCopy(1L, updatedDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().get("message").contains("updated"));
        verify(bookCopyRepository, times(1)).save(any(BookCopy.class));
    }


    @Test
    void testUpdateBookCopyNotFound() {
        BookCopyDTO updatedDTO = new BookCopyDTO(1L, "Fair", "Available");

        when(bookCopyRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                bookCopyService.updateBookCopy(1L, updatedDTO)
        );

        // Update to match the real message thrown
        assertEquals("BookCopy not found", exception.getMessage());
        verify(bookCopyRepository, times(1)).findById(1L);
    }


    @Test
    void testDeleteBookCopy() {
        when(bookCopyRepository.existsById(1L)).thenReturn(true);
        when(bookCopyRepository.getById(1L)).thenReturn(bookCopy);
        when(bookCopyRepository.save(any(BookCopy.class))).thenReturn(bookCopy);

        ResponseEntity<Map<String, String>> response = bookCopyService.deleteBookCopy(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().get("message").contains("deleted"));
        verify(bookCopyRepository, times(1)).save(any(BookCopy.class));
    }

    @Test
    void testDeleteBookCopyWhenBookCopyNotFound() {
        when(bookCopyRepository.existsById(1L)).thenReturn(false);

        ResponseEntity<Map<String, String>> response = bookCopyService.deleteBookCopy(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("BookCopy not found", response.getBody().get("error"));
        verify(bookCopyRepository, times(1)).existsById(1L);
    }
}
