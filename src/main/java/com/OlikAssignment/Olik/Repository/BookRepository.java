package com.OlikAssignment.Olik.Repository;

import com.OlikAssignment.Olik.DataModels.Author;
import com.OlikAssignment.Olik.DataModels.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {
    //List<Book> findByAuthor(String author);

//    List<Book> findByRentedIsFalse();
//
//
//    List<Book> findByRentedIsTrue();
//
//    Book findByBookId(Long bookId);
}


