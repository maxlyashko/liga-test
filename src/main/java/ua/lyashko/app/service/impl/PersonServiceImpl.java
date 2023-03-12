package ua.lyashko.app.service.impl;

import org.springframework.stereotype.Service;
import ua.lyashko.app.entity.Person;
import ua.lyashko.app.repository.PersonRepository;
import ua.lyashko.app.service.PersonService;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    public PersonServiceImpl ( PersonRepository personRepository ) {
        this.personRepository = personRepository;
    }

    @Override
    public Person getPersonById ( Integer id ) {
        return personRepository.findById ( id ).get ( );
    }
}
