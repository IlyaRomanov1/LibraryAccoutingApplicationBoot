package ru.romanov.springcourse.project2Boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.romanov.springcourse.project2Boot.models.Book;


public interface BooksRepository extends JpaRepository<Book, Integer> {
}
