package com.chandu.quiz_service.service;

import com.chandu.quiz_service.feign.QuizInterface;
import com.chandu.quiz_service.model.QuestionWrapper;
import com.chandu.quiz_service.model.Quiz;
import com.chandu.quiz_service.model.Response;
import com.chandu.quiz_service.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService
{
    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizInterface quizInterface;

    public ResponseEntity<?> createQuiz(String category, Integer numQ, String title)
    {
        List<Integer> questions=quizInterface.getQuestionsForQuiz(category,numQ).getBody();

        Quiz quiz=new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);

        return new ResponseEntity<>(quizRepository.save(quiz), HttpStatus.CREATED);

    }

    public ResponseEntity<?> getQuizQuestions(Integer id)
    {
        Quiz quiz=quizRepository.findById(id).get();
        List<Integer> questionIds=quiz.getQuestionIds();

        List<QuestionWrapper> questionsForUser=quizInterface.getQuestionsFromId(questionIds).getBody();
        return new ResponseEntity<>(questionsForUser,HttpStatus.OK);
    }

    public ResponseEntity<?> calculateResult(Integer id, List<Response> responses)
    {
        Integer score=quizInterface.getScore(responses).getBody();
        return new ResponseEntity<>(score,HttpStatus.OK);
    }
}
