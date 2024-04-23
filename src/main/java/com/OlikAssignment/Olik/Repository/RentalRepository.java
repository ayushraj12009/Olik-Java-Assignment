package com.OlikAssignment.Olik.Repository;

import com.OlikAssignment.Olik.DataModels.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    boolean existsByBookIdAndReturnDateIsNull(Long bookId);
    List<Rental> findByReturnDateNull();

//    @Query("SELECT r FROM Rental r WHERE r.returnDate IS NULL AND r.dueDate < :currentDate")
//    List<Rental> findOverdueRentals(@Param("currentDate") LocalDate currentDate);
}

