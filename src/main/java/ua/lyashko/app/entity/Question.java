package ua.lyashko.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "description")
    private String description;

    @Column(name = "length")
    private int length;

    @Override
    public boolean equals ( Object o ) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass ( this ) != Hibernate.getClass ( o )) return false;
        Question question = (Question) o;
        return id != null && Objects.equals ( id , question.id );
    }

    @Override
    public int hashCode () {
        return getClass ( ).hashCode ( );
    }


    public Question ( String description , int length ) {
        this.description = description;
        this.length = length;
    }
}
