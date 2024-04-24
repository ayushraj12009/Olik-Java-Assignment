package com.OlikAssignment.Olik.DataModels;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Author {

    // this is author entity as per the assignment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

   // private String bookName;

    private String biography;


    // mapping with other table (foreign key == primary key)
    @OneToMany(mappedBy = "author")
    private List<Book> books = new ArrayList<>();

    // this is because we don't want return null value in console
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<Book> getBooks() {
        return books.isEmpty() ? null : books;
    }



}
