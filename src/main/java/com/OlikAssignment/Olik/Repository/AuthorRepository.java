package com.OlikAssignment.Olik.Repository;

import com.OlikAssignment.Olik.DataModels.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
