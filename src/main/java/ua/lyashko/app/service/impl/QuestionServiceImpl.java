package ua.lyashko.app.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ua.lyashko.app.entity.Question;
import ua.lyashko.app.model.SimilarQuestionModel;
import ua.lyashko.app.repository.QuestionRepository;
import ua.lyashko.app.service.QuestionService;
import ua.lyashko.app.service.QuestionServiceThread;

import java.util.*;

import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;

    @PersistenceContext
    private EntityManager entityManager;

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
        List<QuestionServiceThread> threads = new ArrayList<> ( );
        for (Question q : questions) {
            QuestionServiceThread thread = new QuestionServiceThread ( q , question );
            thread.start ( );
            threads.add ( thread );
        }
        for (QuestionServiceThread th : threads) {
            th.join ( );
            if (th.getModel ( ).getSimilarity ( ) > 0) {
                modelList.add ( th.getModel ( ) );
            }
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
    public List<Question> findOrderedByLengthLimitedTo ( int limit ) {
        return entityManager.createQuery ( "SELECT q FROM question q ORDER BY q.length DESC", Question.class).setMaxResults ( limit ).getResultList ();
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
