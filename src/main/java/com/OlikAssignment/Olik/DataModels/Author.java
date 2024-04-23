package com.OlikAssignment.Olik.DataModels;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Author {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String bookName;

    private String biography;


    @OneToMany(mappedBy = "author")
    private List<Book> books = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<Book> getBooks() {
        return books.isEmpty() ? null : books;
    }




}
