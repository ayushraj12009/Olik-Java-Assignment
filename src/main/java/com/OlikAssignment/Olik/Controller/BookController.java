package com.OlikAssignment.Olik.Controller;

import com.OlikAssignment.Olik.DataModels.Author;
import com.OlikAssignment.Olik.DataModels.Book;
import com.OlikAssignment.Olik.Exception.AuthorNotFoundException;
import com.OlikAssignment.Olik.Exception.BookNotFoundException;
import com.OlikAssignment.Olik.Repository.AuthorRepository;
import com.OlikAssignment.Olik.Repository.BookRepository;
//import com.OlikAssignment.Olik.RequestDTO.CreateBookRequest;
import com.OlikAssignment.Olik.Services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    // Endpoint to create a book by directly passing a Book object
    @PostMapping("/createBook")
    public ResponseEntity<?> createBook(@RequestBody Book book) {
        try {
            // Attempt to create the book
            Book createdBook = bookService.createBook(book);
            // Return success response with the created book
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
        } catch (IllegalArgumentException e) {
            // Handle invalid ISBN exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while creating the book");
        }
    }

    // Endpoint to create a book by providing author ID and book details
//    @PostMapping("/createBookByAuthorId")
//    public ResponseEntity<Object> createBook(@RequestBody CreateBookRequest request) {
//        try {
//            // Find the author by ID
//            Optional<Author> optionalAuthor = authorRepository.findById(request.getAuthor_id());
//            if (!optionalAuthor.isPresent()) {
//                throw new RuntimeException("Invalid author ID. Please check.");
//            }
//            Author author = optionalAuthor.get();
//
//            // Create a new book
//            Book book = new Book();
//            book.setAuthorName(author);
//          //  book.setTitle(author.getBookName()); // Set the title to author's name (Change if necessary)
//            book.setIsbn(request.getIsbn());
//            book.setAvailable(request.isAvailable());
//            book.setPublicationYear(request.getPublicationYear());
//
//            // Save the book
//            Book savedBook = bookRepository.save(book);
//            // Return success response with the saved book
//            return ResponseEntity.ok(savedBook);
//        } catch (RuntimeException e) {
//            // Handle runtime exceptions
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }


    // Endpoint to get all books
    @GetMapping("/getAllBooks")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }


    // Endpoint to find a book by its ID
    @GetMapping("/findBookByID/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        try {
            // Retrieve the book by ID
            Book book = bookService.getBookById(id);
            // Return success response with the retrieved book
            return ResponseEntity.ok().body(book);
        } catch (BookNotFoundException e) {
            // Handle book not found exception
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book not found with ID: " + id);
        }
    }




    // Endpoint to update a book by its ID
    @PutMapping("/updateBook/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody Book book) {
        try {
            // Update the book
            Book updatedBook = bookService.updateBook(id, book);
            // Return success response with the updated book
            return ResponseEntity.ok().body(updatedBook);
        } catch (BookNotFoundException e) {
            // Handle book not found exception
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book not found with ID: " + id);
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Book not found with ID: " + id);
        }
    }



    // Endpoint to delete a book by its ID
    @DeleteMapping("/deleteBookById/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        try {
            // Delete the book
            bookService.deleteBook(id);
            // Return success response
            return ResponseEntity.ok().body("Book with ID " + id + " has been successfully deleted.");
        } catch (BookNotFoundException e) {
            // Handle book not found exception
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book with ID " + id + " not found.");
        }
    }




}