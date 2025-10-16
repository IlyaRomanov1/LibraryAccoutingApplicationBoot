package ru.romanov.springcourse.project2Boot.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.romanov.springcourse.project2Boot.dao.BookDAO;
import ru.romanov.springcourse.project2Boot.models.Assigment;
import ru.romanov.springcourse.project2Boot.models.Book;
import ru.romanov.springcourse.project2Boot.models.Person;
import ru.romanov.springcourse.project2Boot.services.BooksService;
import ru.romanov.springcourse.project2Boot.services.PeopleService;


@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;
    private final BookDAO bookDAO;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService, BookDAO bookDAO) {
        this.booksService = booksService;
        this.peopleService = peopleService;
        this.bookDAO = bookDAO;
    }

    @GetMapping
    public String index(Model model, @RequestParam(name="page", required = false) Integer page,
    @RequestParam(name="books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(name = "sort_by_year", required = false) boolean sortByYear)
    {

            model.addAttribute("books", booksService.findAll(page, booksPerPage, sortByYear));
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model){
        model.addAttribute("book", booksService.findOne(id));
        model.addAttribute("people", peopleService.findAll());
        model.addAttribute("assigment", new Assigment());
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(Model model){
        model.addAttribute("book", new Book());
        return "books/new";
    }

    @PostMapping()
    public String add(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult){

        if(bindingResult.hasErrors())
            return "/books/new";

        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") Integer id, Model model){
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String edit(@PathVariable("id") Integer id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult){

        if(bindingResult.hasErrors())
            return "/books/edit";

        booksService.update(book, id);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Integer id){
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") Integer id, @ModelAttribute("person") Person person)
    {
        bookDAO.assign(id, person);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/free")
    public String free(@PathVariable("id") Integer id){
        bookDAO.free(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String search(Model model){
        model.addAttribute("searchQuery", "");
        return "books/search";
    }

    @PostMapping("/find")
    public String find(@RequestParam("str") String str, Model model) {
        try {
            Book book = bookDAO.getBook(str);

            if (book != null) {
                model.addAttribute("book", book);
                model.addAttribute("person", book.getReader());
                return "books/search"; // Остаемся на той же странице
            } else {
                model.addAttribute("message", "Not found book");
                return "books/search";
            }
        } catch (Exception e) {
            return "books/search";
        }
    }
}
