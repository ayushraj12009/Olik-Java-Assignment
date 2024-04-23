package com.OlikAssignment.Olik.Services;

import com.OlikAssignment.Olik.DataModels.Author;
import com.OlikAssignment.Olik.Exception.AuthorNotFoundException;
import com.OlikAssignment.Olik.Repository.AuthorRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author getAuthorById(Long id) throws Exception {
        try {
            Optional<Author> authorOptional = authorRepository.findById(id);

            if (authorOptional.isPresent()) {
                return authorOptional.get();
            }
            else {
                throw new RuntimeException("Author not found. Author ID: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while retrieving author. Author ID: " + id);
        }
    }

    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    public Author updateAuthor(Long id, Author authorDetails) {
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        if (optionalAuthor.isPresent()) {
            Author author = optionalAuthor.get();
            author.setName(authorDetails.getName());
            author.setBiography(authorDetails.getBiography());
            return authorRepository.save(author);
        } else {
            throw new AuthorNotFoundException("Author with ID " + id + " not found.");
        }
    }
    public void deleteAuthor(Long id) {
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        if(optionalAuthor.isPresent()){
            authorRepository.deleteById(id);
        }
        else {
            throw new AuthorNotFoundException("Author with ID " + id + " not found.");
        }
    }


}
