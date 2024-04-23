package com.OlikAssignment.Olik.Services;

import com.OlikAssignment.Olik.DataModels.Book;
import com.OlikAssignment.Olik.DataModels.Rental;
import com.OlikAssignment.Olik.Repository.BookRepository;
import com.OlikAssignment.Olik.Repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.ZoneId;
import java.util.*;
import java.time.LocalDate;
import java.util.Date;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private BookRepository bookRepository;

    // Method to rent a book
    public Rental rentBook(Long bookId, String renterName, Date rentalDate)  {
        try {
            // Find the book by its ID
            Optional<Book> optionalBook = bookRepository.findById(bookId);

            // Check if the book exists
            if (optionalBook.isPresent()) {
                Book book = optionalBook.get();

                // Check if the book is available for rent
                if (book.isAvailable()) {
                    // Create a new rental object
                    Rental rental = new Rental();
                    rental.setBook(book);
                    rental.setRenterName(renterName);
                    rental.setRentalDate(rentalDate);

                    // Calculate the return date (14 days from rental date)
                    LocalDate returnLocalDate = rentalDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(14);
                    Date returnDate = Date.from(returnLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    rental.setReturnDate(returnDate);

                    // Set the book as unavailable (rented)
                    book.setAvailable(false);

                    // Save the changes to the book
                    bookRepository.save(book);

                    // Save the rental record
                    return rentalRepository.save(rental);
                } else {
                    // Throw exception if the book is already rented
                    throw new RuntimeException("Book is already rented.");
                }
            } else {
                // Throw exception if the book is not found
                throw new RuntimeException("Book not found.");
            }
        } catch (NoSuchElementException e) {
            // Throw exception if the book ID is invalid
            throw new RuntimeException("Invalid Book ID");
        }
    }


    // Method to return a rented book
    public Rental returnBook(Long rentalId, Date returnDate) {
        try {
            // Find the rental record by its ID
            Optional<Rental> optionalRental = rentalRepository.findById(rentalId);

            // Check if the rental record exists
            if (optionalRental.isPresent()) {
                Rental rental = optionalRental.get();

                // Set the return date of the rental
                rental.setReturnDate(returnDate);

                // Set the book as available (returned)
                rental.getBook().setAvailable(true);

                // Save the changes to the book's availability status
                bookRepository.save(rental.getBook());

                // Calculate overdue days (if any)
                int overdueDays = calculateOverdueDays(rental, returnDate);

                // Set the overdue days in the rental record
                rental.setOverdueDays(overdueDays);

                // Save the updated rental record
                return rentalRepository.save(rental);
            } else {
                // Throw exception if the rental record is not found
                throw new RuntimeException("Rental not found.");
            }
        } catch (Exception e) {
            // Throw exception if an error occurs during the process
            throw new RuntimeException("Error returning the book: " + e.getMessage());
        }
    }



    // Method to calculate overdue days for a rental
    private int calculateOverdueDays(Rental rental, Date returnDate) {
        // Get the rental date from the rental object
        Date rentalDate = rental.getRentalDate();

        // Validate the return date
        if (returnDate == null) {
            throw new IllegalArgumentException("Invalid return date");
        }

        // Calculate the difference between return date and rental date in milliseconds
        long diffInMillies = returnDate.getTime() - rentalDate.getTime();
        // Convert milliseconds to days
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);

        // If the book was returned before the rental date, consider it on time (no overdue days)
        if (diffInDays < 0) {
            return 0;
        }

        // Calculate the number of days the book is overdue (assuming the rental period is 14 days)
        long overdueDays = diffInDays - 14;

        // Ensure that overdue days are non-negative
        return (int) Math.max(overdueDays, 0);
    }




}
