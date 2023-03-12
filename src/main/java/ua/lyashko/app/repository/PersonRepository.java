package ua.lyashko.app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.lyashko.app.entity.Person;

@Repository
public interface PersonRepository  extends CrudRepository<Person, Integer> {
}
