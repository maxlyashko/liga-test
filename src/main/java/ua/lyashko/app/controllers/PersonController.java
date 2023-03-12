package ua.lyashko.app.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ua.lyashko.app.entity.Person;
import ua.lyashko.app.model.PersonModel;
import ua.lyashko.app.service.PersonService;

import java.time.LocalDate;
import java.time.Period;

@RestController
public class PersonController {

    private final PersonService personService;

    public PersonController ( PersonService personService ) {
        this.personService = personService;
    }

    @GetMapping("/person/{id}")
    public PersonModel getPerson ( @PathVariable Integer id ) {
        Person person = personService.getPersonById ( id );
        PersonModel personModel = new PersonModel ( );
        personModel.setName ( person.getFirstName ( ) );
        personModel.setLastName ( person.getLastName ( ) );
        personModel.setAge ( Period.between ( person.getBirthDate ( ) , LocalDate.now ( ) ).getYears ( ) );
        return personModel;
    }

}
