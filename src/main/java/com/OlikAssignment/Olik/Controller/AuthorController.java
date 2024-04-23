package com.OlikAssignment.Olik.Controller;

import com.OlikAssignment.Olik.DataModels.Author;
import com.OlikAssignment.Olik.Exception.AuthorNotFoundException;
import com.OlikAssignment.Olik.Services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    // Get all authors' details
    @GetMapping("/getAllAuthorsDetails")
    public List<Author> getAllAuthors() {
        // Return all authors from the service
        return authorService.getAllAuthors();
    }

    // Get author details by ID
    @GetMapping("/authorDetailsById/{id}")
    public ResponseEntity<?> getAuthorById(@PathVariable Long id) {
        try {
            Author author = authorService.getAuthorById(id);
            // Return author details if found
            return ResponseEntity.ok().body(author);
        } catch (Exception e) {
            // Return error message if author not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Author with ID " + id + " not found.");
        }
    }

    // Create a new author
    @PostMapping("/createAuthor")
    public Author createAuthor(@RequestBody Author author) {
        // Return the created author
        return authorService.createAuthor(author);
    }


    // Update an existing author
    @PutMapping("/updateAuthor/{id}")
    public ResponseEntity<?> updateAuthor(@PathVariable Long id, @RequestBody Author author) {
        try {
            Author updatedAuthor = authorService.updateAuthor(id, author);
            // Return the updated author details
            return ResponseEntity.ok().body(updatedAuthor);
        } catch (AuthorNotFoundException e) {
            // Return error message if author not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Author with ID " + id + " not found.");
        }
    }

    // Delete an author by ID
    @DeleteMapping("/deleteAuthorById/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) {
        try {
            authorService.deleteAuthor(id);
            // Return success message if valid
            return ResponseEntity.ok().body("Author with ID " + id + " has been successfully deleted.");
        } catch (AuthorNotFoundException e) {
            // Return error message if author not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Author with ID " + id + " not found.");
        }
    }
}
