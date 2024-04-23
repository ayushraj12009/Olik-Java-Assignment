package com.OlikAssignment.Olik.Services;

import com.OlikAssignment.Olik.DataModels.Author;
import com.OlikAssignment.Olik.Exception.AuthorNotFoundException;
import com.OlikAssignment.Olik.Repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {


    @Autowired
    private AuthorRepository authorRepository;

    // endpiubt to get all authors from the repository
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    // Method to get an author by ID from the repository
    public Author getAuthorById(Long id) throws Exception {
        try {
            Optional<Author> authorOptional = authorRepository.findById(id);

            if (authorOptional.isPresent()) {
                // Return the author if found
                return authorOptional.get();
            }
            else {
                // Throw exception if author not found
                throw new RuntimeException("Author not found. Author ID: " + id);
            }
        } catch (Exception e) {
            // Throw exception if an error occurs
            throw new RuntimeException("Error occurred while retrieving author. Author ID: " + id);
        }
    }

    // Method to create a new author and save it to the repository
    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    // Method to update an existing author in the repository
    public Author updateAuthor(Long id, Author authorDetails) {
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        if (optionalAuthor.isPresent()) {
            Author author = optionalAuthor.get();
            author.setName(authorDetails.getName());
            author.setBiography(authorDetails.getBiography());
            return authorRepository.save(author);
        } else {
            // Throw exception if author not found
            throw new AuthorNotFoundException("Author with ID " + id + " not found.");
        }
    }

    // Method to delete an author from the repository by ID
    public void deleteAuthor(Long id) {
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        if(optionalAuthor.isPresent()){
            // Delete the author if found
            authorRepository.deleteById(id);
        }
        else {
            // Throw exception if author not found
            throw new AuthorNotFoundException("Author with ID " + id + " not found.");
        }
    }


}
