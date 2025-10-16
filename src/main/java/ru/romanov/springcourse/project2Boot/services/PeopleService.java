package ru.romanov.springcourse.project2Boot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romanov.springcourse.project2Boot.models.Person;
import ru.romanov.springcourse.project2Boot.repositories.PeopleRepository;


import java.util.List;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll(){
        return peopleRepository.findAll();
    }

    public Person findOne(Integer id){
        return peopleRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Person person){
        peopleRepository.save(person);
    }

    @Transactional
    public void update(Person person, Integer id){
        person.setId(id);
        peopleRepository.save(person);
    }

    @Transactional
    public void delete(Integer id){
        peopleRepository.deleteById(id);
    }


}
