package ua.lyashko.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ua.lyashko.app.entity.Person;
import ua.lyashko.app.model.PersonModel;
import ua.lyashko.app.service.PersonService;

import java.time.LocalDate;
import java.time.Period;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-person-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-person-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AppApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonService personService;

    @Test
    void findById () {
        Person test = new Person ( "Java" , "App" , LocalDate.of ( 2000 , 9 , 16 ) );
        Person data = personService.getPersonById ( 1 );
        Assertions.assertEquals ( test.getFirstName () , data.getFirstName () );
    }

    @Test
    void compareAge () {
        PersonModel personModel = new PersonModel ( "Java" , "App" , 22 );
        Person person = personService.getPersonById ( 1 );
        PersonModel fromData = new PersonModel ( person.getFirstName (), person.getLastName (), Period.between ( person.getBirthDate (), LocalDate.now () ).getYears () );
        Assertions.assertEquals ( personModel.getAge (), fromData.getAge () );
    }

    @Test
    void checkStatus () throws Exception {
        this.mockMvc.perform ( get ( "/person/{id}" , "1" ) )
                .andDo ( print ( ) )
                .andExpect ( status ( ).isOk ( ) );
    }
}
