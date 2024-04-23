package com.OlikAssignment.Olik.Services;

import com.OlikAssignment.Olik.DataModels.Author;
import com.OlikAssignment.Olik.DataModels.Book;
import com.OlikAssignment.Olik.Exception.AuthorNotFoundException;
import com.OlikAssignment.Olik.Exception.BookNotFoundException;
import com.OlikAssignment.Olik.Repository.BookRepository;
import com.OlikAssignment.Olik.Repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;


    private static final String ISBN_PATTERN = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$|\\d{13}$)[\\d-]+$";

    public boolean isValidISBN(String isbn) {
        return isbn != null && isbn.matches(ISBN_PATTERN);
    }

    public Book createBook(Book book) {
        try {
            if (!isValidISBN(book.getIsbn())) {
                throw new IllegalArgumentException("Invalid ISBN: " + book.getIsbn());
            }
            return bookRepository.save(book);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating the book", e);
        }
    }


    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }


      public Book getBookById(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            return bookOptional.get();
        } else {
            throw new BookNotFoundException("Book not found. Book ID: " + id);
        }
    }


    public Book updateBook(Long id, Book bookDetails) throws Exception {
        try {
            Book book = getBookById(id);
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setIsbn(bookDetails.getIsbn());
            book.setPublicationYear(bookDetails.getPublicationYear());
            return bookRepository.save(book);
        } catch (BookNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while updating the book. Book ID: " + id);
        }
    }

    public void deleteBook(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if(optionalBook.isPresent()){
            bookRepository.deleteById(id);
        }
        else {
            throw new AuthorNotFoundException("Book with ID " + id + " not found.");
        }
    }


}
