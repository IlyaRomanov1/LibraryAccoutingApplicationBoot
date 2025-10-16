package ru.romanov.springcourse.project2Boot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romanov.springcourse.project2Boot.models.Book;
import ru.romanov.springcourse.project2Boot.repositories.BooksRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public static boolean needPagination(Integer page, Integer nums){
        if(page == null || nums == null)
            return false;
        return true;
    }

    public List<Book> findAll(Integer page, Integer booksPerPage, boolean sortByYear){

        boolean f = needPagination(page, booksPerPage);

            if(sortByYear)
                if(f)
                    return booksRepository.findAll(PageRequest.of(page-1, booksPerPage,
                        Sort.by("yearOfProduction"))).getContent();
                 else
                    return booksRepository.findAll(Sort.by("yearOfProduction"));
            else
                if(f)
                    return booksRepository.findAll(PageRequest.of(page-1, booksPerPage)).getContent();
       return booksRepository.findAll();
    }

    public Book findOne(Integer id){
        return booksRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Book book){
        booksRepository.save(book);
    }

    @Transactional
    public void update(Book book, Integer id){
        book.setId(id);
        booksRepository.save(book);
    }

    @Transactional
    public void delete(Integer id){
        booksRepository.deleteById(id);
    }
}
