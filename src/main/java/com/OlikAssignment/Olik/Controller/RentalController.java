package com.OlikAssignment.Olik.Controller;


import com.OlikAssignment.Olik.DataModels.Book;
import com.OlikAssignment.Olik.DataModels.Rental;
import com.OlikAssignment.Olik.Repository.BookRepository;
import com.OlikAssignment.Olik.RequestDTO.RentRequest;
import com.OlikAssignment.Olik.RequestDTO.ReturnRequest;
import com.OlikAssignment.Olik.Services.BookService;
import com.OlikAssignment.Olik.Services.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/rentals")
public class RentalController {
    @Autowired
    private RentalService rentalService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    // Endpoint to retrieve available books for rent
    @GetMapping("/bookForRent")
    public ResponseEntity<List<Book>> availableBookForRent() {
        // Retrieve all books from the repository
        List<Book> allBooks = bookRepository.findAll();
        // List to store available books
        List<Book> availableBooks = new ArrayList<>();

        // Iterate through all books
        for (int i = 0; i < allBooks.size(); i++) {
            // Get a book from the list
            Book book = allBooks.get(i);
            // Check if the book is available for rent
            if (book.isAvailable()) {
                // If available, add it to the list of available books
                availableBooks.add(book);
            }
        }
        // Return response with the list of available books
        return ResponseEntity.ok(availableBooks);
    }


    // Endpoint to rent a book
    @PostMapping("/bookOnRent")
    public ResponseEntity<Object> rentBook(@RequestBody RentRequest rentRequest) {
        // Check if the request object or its fields are null
        if (rentRequest == null || rentRequest.getBookID() == null || rentRequest.getRenterName() == null || rentRequest.getRentalDate() == null) {
            // If any field is null, return a bad request response
            return ResponseEntity.badRequest().body("Invalid request. Please provide book ID, renter name, and rental date.");
        }

        // Extract book ID, renter name, and rental date from the request
        Long bookID = rentRequest.getBookID();
        String renterName = rentRequest.getRenterName();
        Date rentalDate = rentRequest.getRentalDate();

        try {
            // Attempt to rent the book using the rental service
            Rental rentedBook = rentalService.rentBook(bookID, renterName, rentalDate);
            // If successful, return the rented book
            return ResponseEntity.ok(rentedBook);
        } catch (RuntimeException e) {
            // If an exception occurs, return a not found response with the exception message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



    // Endpoint to return a rented book
    @PostMapping("/returnBook")
    public ResponseEntity<Object> returnBook(@RequestBody ReturnRequest returnRequest) {
        try {
            // Check if the return request or its fields are null
            if (returnRequest == null || returnRequest.getRentalId() == null) {
                // If any field is null, return a bad request response
                return ResponseEntity.badRequest().body("Invalid request. Please provide rental ID.");
            }

            // Extract rental ID and return date from the return request
            Long rentalId = returnRequest.getRentalId();
            Date returnDate = returnRequest.getReturnDate();

            // Attempt to return the book using the rental service
            Rental returnedBook = rentalService.returnBook(rentalId, returnDate);

            // Map to hold response data
            Map<String, Object> response = new HashMap<>();

            // Message based on overdue days
            int overdueDays = returnedBook.getOverdueDays();
            if (overdueDays > 0) {
                response.put("message", "Book returned late by " + overdueDays + " days.");
            } else {
                response.put("message", "Thanks for returning the book!");
            }

            // Add rental object to response
            response.put("rental", returnedBook);

            // Return response with message and rental object
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // If an exception occurs, return a not found response with the exception message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



}