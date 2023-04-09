package ua.lyashko.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.lyashko.app.entity.Question;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
