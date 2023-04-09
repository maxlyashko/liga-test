package ua.lyashko.app.service;

import ua.lyashko.app.entity.Question;
import ua.lyashko.app.model.SimilarQuestionModel;

import java.util.List;

public interface QuestionService {

    List<Question> getAllQuestions ();

    void saveQuestion ( Question question );

    List<Question> findTop5ByLength ();

    List<SimilarQuestionModel> getSimilarQuestionsList ( String target , int quantity );
}
