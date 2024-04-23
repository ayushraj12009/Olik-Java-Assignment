package com.OlikAssignment.Olik.Controller;

import com.OlikAssignment.Olik.DataModels.Author;
import com.OlikAssignment.Olik.DataModels.Book;
import com.OlikAssignment.Olik.Exception.AuthorNotFoundException;
import com.OlikAssignment.Olik.Exception.BookNotFoundException;
import com.OlikAssignment.Olik.Repository.AuthorRepository;
import com.OlikAssignment.Olik.Repository.BookRepository;
import com.OlikAssignment.Olik.RequestDTO.CreateBookRequest;
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

    @PostMapping("/createBook")
    public ResponseEntity<?> createBook(@RequestBody Book book) {
        try {
            Book createdBook = bookService.createBook(book);
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

    @PostMapping("/createBookByAuthorId")
    public ResponseEntity<Object> createBook(@RequestBody CreateBookRequest request) {
        try {
            Optional<Author> optionalAuthor = authorRepository.findById(request.getAuthor_id());
            if (!optionalAuthor.isPresent()) {
                throw new RuntimeException("Invalid author ID. Please check.");
            }
            Author author = optionalAuthor.get();

            Book book = new Book();

            book.setAuthorName(author);
            book.setAuthor(author.getName());
            book.setTitle(author.getName());
            book.setIsbn(request.getIsbn());
            book.setAvailable(request.isAvailable());
            book.setPublicationYear(request.getPublicationYear());


            Book savedBook = bookRepository.save(book);
            return ResponseEntity.ok(savedBook);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAllBooks")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/findBookByID/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        try {
            Book book = bookService.getBookById(id);
            return ResponseEntity.ok().body(book);
        } catch (BookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book not found with ID: " + id);
        }
    }



    @PutMapping("/updateBook/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody Book book) {
        try {
            Book updatedBook = bookService.updateBook(id, book);
            return ResponseEntity.ok().body(updatedBook);
        } catch (BookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book not found with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Book not found with ID: " + id);
        }
    }


    @DeleteMapping("/deleteBookById/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok().body("Book with ID " + id + " has been successfully deleted.");
        } catch (AuthorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book with ID " + id + " not found.");
        }
    }



}