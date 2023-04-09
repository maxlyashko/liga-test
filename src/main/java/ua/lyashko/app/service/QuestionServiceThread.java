package ua.lyashko.app.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import ua.lyashko.app.entity.Question;
import ua.lyashko.app.model.SimilarQuestionModel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionServiceThread extends Thread {

    private Question q;
    private Question question;
    private SimilarQuestionModel model = new SimilarQuestionModel ( );

    private int findSimilarity ( Question q1 , Question q2 ) {
        List<String> q1List = getPreparedList ( q1 );
        List<String> q2List = getPreparedList ( q2 );
        if (Objects.equals ( q1List.get ( 0 ) , q2List.get ( 0 ) )) {
            return Math.toIntExact ( q2List.stream ( )
                    .filter ( q1List::contains )
                    .count ( ) );
        }
        return 0;
    }

    private List<String> getPreparedList ( @NotNull Question q ) {
        return Arrays.stream ( q.getDescription ( ).trim ( ).split ( "[ ]|[.]|[,]|[ \t]" ) )
                .map ( String::toUpperCase )
                .filter ( s -> s.length ( ) >= 3 )
                .collect ( Collectors.toList ( ) );
    }

    @Override
    public void run () {
        model.setDescription ( q.getDescription ( ) );
        model.setSimilarity ( findSimilarity ( question , q ) );
    }

    public QuestionServiceThread ( Question q , Question question ) {
        this.q = q;
        this.question = question;
    }
}
