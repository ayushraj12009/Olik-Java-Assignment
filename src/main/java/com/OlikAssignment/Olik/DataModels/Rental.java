package com.OlikAssignment.Olik.DataModels;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Rental {

    // this is book entity as per the given assignment

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String renterName;

    // Field to store the date when the book was rented
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date rentalDate;

    // Field to store the date when the rented book is expected to be returned
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date returnDate;
    private int overdueDays;

    // Many-to-one relationship field linking Rental to Book entity
    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    public Rental(String renterName, Date rentalDate, Date returnDate, int overdueDays, Book book) {
        this.renterName = renterName;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.overdueDays = overdueDays;
        this.book = book;
    }


}
