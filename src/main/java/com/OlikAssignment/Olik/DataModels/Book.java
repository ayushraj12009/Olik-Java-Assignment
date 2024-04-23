package com.OlikAssignment.Olik.DataModels;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    // this is book entity as per the given assignment
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private int publicationYear;
    private boolean available;


    public boolean isAvailable() {
        return available;
    }

    // this is because we don't want return null value in console
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne
    // mapping with other table (foreign key == primary key)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Author authorName;

}
