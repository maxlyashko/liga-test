package ua.lyashko.app.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import ua.lyashko.app.entity.Question;
import ua.lyashko.app.model.SimilarQuestionModel;
import ua.lyashko.app.service.QuestionService;

import java.util.List;

@RestController
public class QuestionController {
    private final QuestionService questionService;


    public QuestionController ( QuestionService questionService ) {
        this.questionService = questionService;
    }

    @GetMapping("/top")
    @Operation(description = "An endpoint to extract top X questions by length from database")
    public List<Question> getTop (@RequestParam(value = "limit", required = false) int limit) {
        return questionService.findOrderedByLengthLimitedTo (limit );
    }

    @PostMapping("/similar")
    @Operation(description = "An endpoint to find similar questions and return list of it with required size to user")
    public List<SimilarQuestionModel> getSimilar ( @RequestParam(value = "question", required = false) String question ,
                                                   @RequestParam(value = "quantity", required = false) Integer quantity ) {
        return questionService.getSimilarQuestionsList ( question , quantity );
    }
}
