package com.OlikAssignment.Olik.Services;

import com.OlikAssignment.Olik.DataModels.Book;
import com.OlikAssignment.Olik.DataModels.Rental;
import com.OlikAssignment.Olik.Repository.BookRepository;
import com.OlikAssignment.Olik.Repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
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

    public Rental rentBook(Long bookId, String renterName, Date rentalDate)  {
        try {
            Optional<Book> optionalBook = bookRepository.findById(bookId);

            if (optionalBook.isPresent()) {
                Book book = optionalBook.get();
                if (book.isAvailable()) {
                    Rental rental = new Rental();
                    rental.setBook(book);
                    //rental.setBookID(bookId);
                    rental.setRenterName(renterName);
                    rental.setRentalDate(rentalDate);

                    LocalDate returnLocalDate = rentalDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(14);
                    Date returnDate = Date.from(returnLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

                    rental.setReturnDate(returnDate);

                    book.setAvailable(false);
                    bookRepository.save(book);
                    return rentalRepository.save(rental);
                } else {
                    throw new RuntimeException("Book is already rented.");
                }
            } else {
                throw new RuntimeException("Book not found.");
            }
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Invalid Book ID");
        }
    }



    public Rental returnBook(Long rentalId, Date returnDate) {
        try {
            Optional<Rental> optionalRental = rentalRepository.findById(rentalId);

            if (optionalRental.isPresent()) {
                Rental rental = optionalRental.get();
                rental.setReturnDate(returnDate);
                rental.getBook().setAvailable(true);
                bookRepository.save(rental.getBook());

                int overdueDays = calculateOverdueDays(rental,returnDate);

//                if (overdueDays > 0) {
//                    System.out.println("Book returned late by " + overdueDays + " days.");
//                } else {
//                    System.out.println("Thanks for returning the book!");
//                }

                rental.setOverdueDays(overdueDays);
                return rentalRepository.save(rental);
            } else {
                throw new RuntimeException("Rental not found.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error returning the book: " + e.getMessage());
        }
    }



    // Method to calculate overdue days
    private int calculateOverdueDays(Rental rental, Date returnDate) {
        Date rentalDate = rental.getRentalDate();
        //Date returnDate = rental.getReturnDate();

        if (returnDate == null) {
            throw new IllegalArgumentException("Invalid return date");
        }

        long diffInMillies = returnDate.getTime() - rentalDate.getTime();
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);

        // If the book was returned before the rental date, consider it on time
        if (diffInDays < 0) {
            return 0;
        }
        long overdueDays = diffInDays - 14;

        return (int) Math.max(overdueDays, 0);
    }





}
