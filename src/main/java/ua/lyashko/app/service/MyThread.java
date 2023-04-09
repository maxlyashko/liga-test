package ua.lyashko.app.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.jetbrains.annotations.NotNull;
import ua.lyashko.app.entity.Question;
import ua.lyashko.app.model.SimilarQuestionModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class MyThread extends Thread {

    private Question q;
    private Question question;
    private List<SimilarQuestionModel> modelList;

    private int findSimilarity ( Question q1 , Question q2 ) {
        int similarity = 0;
        List<String> q1List = getPreparedList ( q1 );
        List<String> q2List = getPreparedList ( q2 );
        List<String> temp = new ArrayList<> ( );
        if (Objects.equals ( q1List.get ( 0 ) , q2List.get ( 0 ) )) {
            q2List.stream ( )
                    .filter ( q1List::contains )
                    .forEach ( temp::add );
            similarity = temp.size ( );
        }
        return similarity;
    }


    private void addToModelList ( SimilarQuestionModel model ) {
        if (model.getSimilarity ( ) > 0) {
            modelList.add ( model );
        }
    }

    private List<String> getPreparedList ( @NotNull Question q ) {
        return Arrays.stream ( q.getDescription ( ).trim ( ).split ( "[ ]|[.]|[,]|[ \t]" ) )
                .map ( String::toUpperCase )
                .filter ( s -> s.length ( ) >= 3 )
                .collect ( Collectors.toList ( ) );
    }

    @Override
    public void run () {
        SimilarQuestionModel model = new SimilarQuestionModel ( );
        model.setDescription ( q.getDescription ( ) );
        model.setSimilarity ( findSimilarity ( question , q ) );
        addToModelList ( model );
    }
}
