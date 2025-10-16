package ru.romanov.springcourse.project2Boot.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.romanov.springcourse.project2Boot.dao.PersonDAO;
import ru.romanov.springcourse.project2Boot.models.Person;
import ru.romanov.springcourse.project2Boot.services.PeopleService;


@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;
    private final PersonDAO personDAO;

    public PeopleController(PeopleService peopleService, PersonDAO personDAO) {
        this.peopleService = peopleService;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model){
        model.addAttribute("person", peopleService.findOne(id));
        model.addAttribute("books", personDAO.getPersonBooks(id));

        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(Model model){
        model.addAttribute("person", new Person());
        return "people/new";
    }

    @PostMapping()
    public String add(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){

        if(bindingResult.hasErrors())
            return "/people/new";

        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(@PathVariable("id") Integer id, Model model){
        model.addAttribute("person", peopleService.findOne(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String edit(@PathVariable("id") Integer id, @ModelAttribute("person") @Valid Person person, BindingResult bindingResult){

        if(bindingResult.hasErrors())
            return "/people/edit";

        peopleService.update(person, id);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Integer id){
        peopleService.delete(id);
        return "redirect:/people";
    }
}
