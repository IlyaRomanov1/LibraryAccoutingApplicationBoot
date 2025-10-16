package ru.romanov.springcourse.project2Boot.dao;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.romanov.springcourse.project2Boot.models.Book;
import ru.romanov.springcourse.project2Boot.models.Person;


import java.util.ArrayList;
import java.util.Collections;

@Component
@Transactional(readOnly = false)
public class BookDAO {

    private final EntityManager entityManager;

    @Autowired
    public BookDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void assign(Integer id, Person person){
        Session session = entityManager.unwrap(Session.class);
        Book book = session.get(Book.class, id);
        book.setReader(person);
        person.setBooks(new ArrayList<>(Collections.singletonList(book)));
    }

    public void free(Integer id){
        Session session = entityManager.unwrap(Session.class);
        Book book = session.get(Book.class, id);
        Person person = book.getReader();
        person.getBooks().remove(book);
        book.setReader(null);
    }

    @Transactional
    public Book getBook(String start){
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("SELECT b FROM Book b WHERE b.tittle LIKE :searchTerm", Book.class)
                .setParameter("searchTerm", start + "%")
                .uniqueResult();
    }

    @Transactional
    public Person getPerson(Integer id){
        Session session = entityManager.unwrap(Session.class);
        Book book = session.get(Book.class, id);
        return book.getReader();
    }
}
