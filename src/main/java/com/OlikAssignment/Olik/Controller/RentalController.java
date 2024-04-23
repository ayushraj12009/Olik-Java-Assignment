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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rentals")
public class RentalController {
    @Autowired
    private RentalService rentalService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;


//    @GetMapping("/author/{authorName}")
//    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable String authorName) {
//        List<Book> books = bookService.getAllBooksByAuthor(authorName);
//        return ResponseEntity.ok().body(books);
//    }



//    @PostMapping("/bookOnRent")
//    public ResponseEntity<Rental> rentBook(@RequestBody RentRequest rentRequest) {
//        if (rentRequest == null || rentRequest.getBookID() == null || rentRequest.getRenterName() == null) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        Long bookID = rentRequest.getBookID();
//        String renterName = rentRequest.getRenterName();
//        Date rentalDate = rentRequest.getRentalDate();
//
//        Rental rentedBook = rentalService.rentBook(bookID, renterName,rentalDate);
//        if (rentedBook == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        return ResponseEntity.ok().body(rentedBook);
//    }


    @PostMapping("/bookOnRent")
    public ResponseEntity<Object> rentBook(@RequestBody RentRequest rentRequest) {
        if (rentRequest == null || rentRequest.getBookID() == null || rentRequest.getRenterName() == null || rentRequest.getRentalDate() == null) {
            return ResponseEntity.badRequest().body("Invalid request. Please provide book ID, renter name, and rental date.");
        }
        Long bookID = rentRequest.getBookID();
        String renterName = rentRequest.getRenterName();
        Date rentalDate = rentRequest.getRentalDate();

        try {
            Rental rentedBook = rentalService.rentBook(bookID, renterName, rentalDate);
            return ResponseEntity.ok(rentedBook);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

//
//    @PostMapping("/returnBook")
//    public ResponseEntity<Object> returnBook(@RequestBody ReturnRequest returnRequest) {
//        try {
//            if (returnRequest == null || returnRequest.getRentalId() == null) {
//                return ResponseEntity.badRequest().body("Invalid request. Please provide rental ID.");
//            }
//
//            Long rentalId = returnRequest.getRentalId();
//            Date returnDate = returnRequest.getReturnDate();
//
//            Rental returnedBook = rentalService.returnBook(rentalId,returnDate);
//
//            return ResponseEntity.ok(returnedBook);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }

    @PostMapping("/returnBook")
    public ResponseEntity<Object> returnBook(@RequestBody ReturnRequest returnRequest) {
        try {
            if (returnRequest == null || returnRequest.getRentalId() == null) {
                return ResponseEntity.badRequest().body("Invalid request. Please provide rental ID.");
            }

            Long rentalId = returnRequest.getRentalId();
            Date returnDate = returnRequest.getReturnDate();

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

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}