package com.OlikAssignment.Olik;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.OlikAssignment.Olik.Controller.BookController;
import com.OlikAssignment.Olik.DataModels.Book;
import com.OlikAssignment.Olik.Services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Test
    public void createBook_ValidBook_ShouldReturnCreated() {
        // Arrange
        Book book = new Book();
        book.setTitle("Book Title");
        book.setAuthor("Author Name");
        book.setIsbn("2524535895");
        book.setPublicationYear(2024);
        book.setAvailable(true);

        Book createdBook = new Book();
        createdBook.setId(1L);
        createdBook.setTitle("Book Title");
        createdBook.setAuthor("Author Name");
        createdBook.setIsbn("2524535895");
        createdBook.setPublicationYear(2024);
        createdBook.setAvailable(true);

        // Mocking the bookService to return the created book
        when(bookService.createBook(any(Book.class))).thenReturn(createdBook);

        // Act
        ResponseEntity<?> responseEntity = bookController.createBook(book);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(createdBook, responseEntity.getBody());
    }

    @Test
    public void createBook_InvalidIsbn_ShouldReturnBadRequest() {
        // Arrange
        Book book = new Book();
        book.setTitle("Book Title");
        book.setAuthor("Author Name");
        book.setPublicationYear(2024);
        book.setAvailable(true);
        book.setIsbn("Invalid-ISBN");

        String errorMessage = "Invalid ISBN";

        // Mocking the bookService to throw an IllegalArgumentException
        when(bookService.createBook(any(Book.class))).thenThrow(new IllegalArgumentException(errorMessage));

        // Act
        ResponseEntity<?> responseEntity = bookController.createBook(book);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(errorMessage, responseEntity.getBody());
    }

    @Test
    public void createBook_InternalServerError_ShouldReturnInternalServerError() {
        // Arrange
        Book book = new Book();
        book.setTitle("Book Title");
        book.setAuthor("Author Name");
        book.setIsbn("2524535895");
        book.setPublicationYear(2024);
        book.setAvailable(true);

        // Mocking the bookService to throw an Exception
        when(bookService.createBook(any(Book.class))).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<?> responseEntity = bookController.createBook(book);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Error occurred while creating the book", responseEntity.getBody());
    }


    @Test
    public void getAllBooks_ShouldReturnAllBooks() {
        // Arrange
        Book book1 = new Book();
        book1.setTitle("Book Title");
        book1.setAuthor("Author Name");
        book1.setIsbn("2524535895");
        book1.setPublicationYear(2024);
        book1.setAvailable(true);

        Book book2 = new Book();
        book2.setTitle("Book Title 2");
        book2.setAuthor("Author Name 2");
        book2.setIsbn("2524335895");
        book2.setPublicationYear(2024);
        book2.setAvailable(true);

        List<Book> expectedBooks = Arrays.asList(book1, book2);

        // Mocking the bookService to return the list of books
        when(bookService.getAllBooks()).thenReturn(expectedBooks);

        // Act
        List<Book> actualBooks = bookController.getAllBooks();

        // Assert
        assertEquals(expectedBooks.size(), actualBooks.size());
        for (int i = 0; i < expectedBooks.size(); i++) {
            assertEquals(expectedBooks.get(i), actualBooks.get(i));
        }
    }




    @Test
    public void getBookById_ExistingId_ShouldReturnBook() {
        // Arrange
        long id = 1L;
        Book book = new Book();
        book.setTitle("Book Title 2");
        book.setAuthor("Author Name 2");
        book.setIsbn("2524335895");
        book.setPublicationYear(2024);
        book.setAvailable(true);

        // Mocking the bookService to return the book
        when(bookService.getBookById(id)).thenReturn(book);

        // Act
        ResponseEntity<?> responseEntity = bookController.getBookById(id);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof Book);
        assertEquals(book, responseEntity.getBody());
    }


    @Test
    public void deleteBook_ExistingId_ShouldReturnSuccessMessage() {
        // Arrange
        long id = 1L;

        // Mocking the bookService to successfully delete the book
        doNothing().when(bookService).deleteBook(id);

        // Act
        ResponseEntity<String> responseEntity = bookController.deleteBook(id);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Book with ID " + id + " has been successfully deleted.", responseEntity.getBody());
    }





}
