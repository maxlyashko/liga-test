package ua.lyashko.app.service.impl;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ua.lyashko.app.entity.Question;
import ua.lyashko.app.model.SimilarQuestionModel;
import ua.lyashko.app.repository.QuestionRepository;
import ua.lyashko.app.service.MyThread;
import ua.lyashko.app.service.QuestionService;

import java.util.*;

import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionServiceImpl ( QuestionRepository questionRepository ) {
        this.questionRepository = questionRepository;
    }

    private int findLength ( @NotNull String target ) {
        return target.trim ( ).split ( "[\\s]+" ).length;
    }

    @SneakyThrows
    private void setSimilarity ( @NotNull Question question ,
                                 @NotNull List<Question> questions ,
                                 @NotNull List<SimilarQuestionModel> modelList ) {
        for (Question q : questions) {
            MyThread myThread = new MyThread ( q , question , modelList );
            myThread.start ( );
            myThread.join ( );
        }
    }

    @Override
    public List<Question> getAllQuestions () {
        return questionRepository.findAll ( );
    }

    @Override
    public void saveQuestion ( @NotNull Question question ) {
        String temp = question.getDescription ( );
        question.setDescription ( temp.trim ( ) );
        questionRepository.save ( question );
    }

    @Override
    public List<Question> findTop5ByLength () {
        return questionRepository.findTop5ByOrderByLengthDesc ( );
    }

    @Override
    public List<SimilarQuestionModel> getSimilarQuestionsList ( String target , int quantity ) {
        Question question = new Question ( target , findLength ( target ) );
        List<Question> questions = getAllQuestions ( );
        List<SimilarQuestionModel> modelList = new ArrayList<> ( );
        setSimilarity ( question , questions , modelList );
        if (modelList.size ( ) == 0) {
            saveQuestion ( question );
        }
        return modelList.stream ( )
                .sorted ( Comparator.comparingInt ( SimilarQuestionModel::getSimilarity ).reversed ( ) )
                .limit ( quantity )
                .collect ( Collectors.toList ( ) );
    }
}
