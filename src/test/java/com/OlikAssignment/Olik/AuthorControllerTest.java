package com.OlikAssignment.Olik;

import com.OlikAssignment.Olik.Controller.AuthorController;
import com.OlikAssignment.Olik.DataModels.Author;
import com.OlikAssignment.Olik.Exception.AuthorNotFoundException;
import com.OlikAssignment.Olik.Services.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


import java.util.Arrays;
import java.util.List;


public class AuthorControllerTest {

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    @BeforeEach
    public void setUp() {
        // Initialize Mockito
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createAuthor_ValidRequest_ShouldReturnCreated() {
        // Arrange
        // Mocked author object
        Author mockedAuthor = new Author();
        mockedAuthor.setName("Ayush Raj");
        mockedAuthor.setBookName("HC Verma");
        mockedAuthor.setBiography("This is good book");

        // Mocking the authorService to return the mocked author
        when(authorService.createAuthor(any(Author.class))).thenReturn(mockedAuthor);

        // Create a request body for the endpoint
        Author requestAuthor = new Author();
        requestAuthor.setName("Ayush Raj");
        requestAuthor.setBookName("HC Verma");
        requestAuthor.setBiography("This is good book");

        // Act
        // Calling the controller method
        Author responseAuthor = authorController.createAuthor(requestAuthor);

        // Assert
        // Checking if the response body matches the mocked author
        assertEquals(mockedAuthor, responseAuthor);
    }



    @Test
    public void getAllAuthors_ShouldReturnAllAuthors() {
        // Arrange
        // Mocked list of authors
        Author author1 = new Author();
        author1.setName("Ayush Raj");
        author1.setBookName("HC Verma");
        author1.setBiography("This is good book");

        Author author2 = new Author();
        author2.setName("Raj");
        author2.setBookName("RS Agarwal");
        author2.setBiography("This is good book for maths");
        List<Author> mockedAuthors = Arrays.asList(author1, author2);

        // Mocking the authorService to return the mocked list of authors
        when(authorService.getAllAuthors()).thenReturn(mockedAuthors);

        // Act
        // Calling the controller method
        List<Author> responseAuthors = authorController.getAllAuthors();

        // Assert
        // Checking if the response body matches the mocked list of authors
        assertEquals(mockedAuthors.size(), responseAuthors.size());
        for (int i = 0; i < mockedAuthors.size(); i++) {
            assertEquals(mockedAuthors.get(i), responseAuthors.get(i));
        }
    }



    @Test
    public void getAuthorById_ExistingId_ShouldReturnAuthor() throws Exception {
        // Arrange
        long existingId = 1L;
        Author mockedAuthor = new Author();
        mockedAuthor.getId();

        // Mocking the authorService to return the mocked author
        when(authorService.getAuthorById(existingId)).thenReturn(mockedAuthor);

        // Act
        ResponseEntity<?> responseEntity = authorController.getAuthorById(existingId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof Author);
        assertEquals(mockedAuthor, responseEntity.getBody());
    }

    @Test
    public void getAuthorById_NonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        long nonExistingId = 100L;

        // Mocking the authorService to throw an exception for non-existing ID
        when(authorService.getAuthorById(nonExistingId)).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<?> responseEntity = authorController.getAuthorById(nonExistingId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Author with ID " + nonExistingId + " not found.", responseEntity.getBody());
    }


    @Test
    public void updateAuthor_ValidIdAndAuthor_ShouldReturnUpdatedAuthor() {
        // Arrange
        long id = 1L;
        Author updatedAuthor = new Author();
        updatedAuthor.setName("Ayush Raj");
        updatedAuthor.setBookName("HC Verma");
        updatedAuthor.setBiography("This is good book");

        // Mocking the authorService to return the updated author
        when(authorService.updateAuthor(anyLong(), any(Author.class))).thenReturn(updatedAuthor);

        // Act
        ResponseEntity<?> responseEntity = authorController.updateAuthor(id, updatedAuthor);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof Author);
        assertEquals(updatedAuthor, responseEntity.getBody());
    }

    @Test
    public void updateAuthor_NonExistingId_ShouldReturnNotFound() {
        // Arrange
        long nonExistingId = 100L;
        Author author = new Author();
        author.setName("Ayush Raj");
        author.setBookName("HC Verma");
        author.setBiography("This is good book");

        // Mocking the authorService to throw an exception for non-existing ID
        when(authorService.updateAuthor(anyLong(), any(Author.class))).thenThrow(AuthorNotFoundException.class);

        // Act
        ResponseEntity<?> responseEntity = authorController.updateAuthor(nonExistingId, author);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Author with ID " + nonExistingId + " not found.", responseEntity.getBody());
    }








}














