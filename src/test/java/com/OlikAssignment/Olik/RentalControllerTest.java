package com.OlikAssignment.Olik;

import com.OlikAssignment.Olik.Controller.BookController;
import com.OlikAssignment.Olik.Controller.RentalController;
import com.OlikAssignment.Olik.DataModels.Rental;
import com.OlikAssignment.Olik.RequestDTO.RentRequest;
import com.OlikAssignment.Olik.RequestDTO.ReturnRequest;
import com.OlikAssignment.Olik.Services.RentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.http.HttpStatus;
import java.util.Date;
import java.util.Map;

public class RentalControllerTest {

    @Mock
    private RentalService rentalService;

    @InjectMocks
    private BookController bookController;


    @InjectMocks
    private RentalController rentalController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void rentBook_ValidRequest_ShouldReturnRentedBook() {
        // Arrange
        RentRequest rentRequest = new RentRequest();
        rentRequest.setBookID(1L);
        rentRequest.setRenterName("Ayush Raj");
        rentRequest.setRentalDate(new Date());

        Rental rentedBook = new Rental();
        rentedBook.setId(1L);
        rentedBook.setRenterName("Ayush Raj");
        rentedBook.setRentalDate(new Date());

        // Mocking the rentalService to return the rented book
        when(rentalService.rentBook(any(Long.class), any(String.class), any(Date.class))).thenReturn(rentedBook);

        // Act
        Rental returnedRental = rentalService.rentBook(rentRequest.getBookID(), rentRequest.getRenterName(), rentRequest.getRentalDate());

        // Assert
        assertNotNull(returnedRental);
        assertEquals(rentedBook.getId(), returnedRental.getId());
        assertEquals(rentedBook.getRenterName(), returnedRental.getRenterName());
        assertEquals(rentedBook.getRentalDate(), returnedRental.getRentalDate());
    }



    @Test
    public void returnBook_ValidRequest_ShouldReturnSuccessMessageAndRentalObject() {
        // Arrange
        ReturnRequest returnRequest = new ReturnRequest();
        returnRequest.setRentalId(1L);
        returnRequest.setReturnDate(new Date());

        Rental returnedBook = new Rental();
        returnedBook.setId(1L);
        returnedBook.setOverdueDays(0); // Assuming the book is not returned late

        // Mocking the rentalService to return the returned book
        when(rentalService.returnBook(any(Long.class), any(Date.class))).thenReturn(returnedBook);

        // Act
        ResponseEntity<Object> responseEntity = rentalController.returnBook(returnRequest);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertEquals("Thanks for returning the book!", responseBody.get("message"));
        assertEquals(returnedBook, responseBody.get("rental"));
    }


    @Test
    public void returnBook_InvalidRequest_ShouldReturnBadRequest() {
        // Arrange
        ReturnRequest returnRequest = new ReturnRequest(); // No fields set, which will be considered invalid

        // Act
        ResponseEntity<Object> responseEntity = rentalController.returnBook(returnRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid request. Please provide rental ID.", responseEntity.getBody());
    }

    @Test
    public void returnBook_ExceptionThrown_ShouldReturnNotFound() {
        // Arrange
        ReturnRequest returnRequest = new ReturnRequest();
        returnRequest.setRentalId(1L);
        returnRequest.setReturnDate(new Date());

        // Mocking the rentalService to throw a RuntimeException
        when(rentalService.returnBook(any(Long.class), any(Date.class)))
                .thenThrow(new RuntimeException("Rental not found."));

        // Act
        ResponseEntity<Object> responseEntity = rentalController.returnBook(returnRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Rental not found.", responseEntity.getBody());
    }


}
