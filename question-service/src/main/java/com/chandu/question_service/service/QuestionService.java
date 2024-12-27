package com.chandu.question_service.service;

import com.chandu.question_service.model.Question;
import com.chandu.question_service.model.QuestionWrapper;
import com.chandu.question_service.model.Response;
import com.chandu.question_service.repository.QuestionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService
{
    @Autowired
    private QuestionRepository questionRepository;

    public ResponseEntity<List<Question>> getAllQuestions()
    {
        try {
            return new ResponseEntity<>(questionRepository.findAll(), HttpStatus.OK);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category)
    {
        try {
            return new ResponseEntity<>(questionRepository.findByCategory(category), HttpStatus.OK);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Question> addQuestion(Question question)
    {
        try {
            return new ResponseEntity<>(questionRepository.save(question), HttpStatus.CREATED);
        }catch (Exception e)
        {
            return new ResponseEntity<>(null,HttpStatus.BAD_GATEWAY);
        }
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String categoryName, int numQuestions)
    {
        List<Integer> questions=questionRepository.findRandomQuestionsByCategory(categoryName,numQuestions);

        return new ResponseEntity<>(questions,HttpStatus.OK);
    }

    public ResponseEntity<?> getQuestionsForId(List<Integer> questionIds)
    {
        List<Question> questions=new ArrayList<>();
        for(int i : questionIds)
        {
            questions.add(questionRepository.findById(i).get());
        }

        List<QuestionWrapper> wrappers=questions.stream().map(q->new QuestionWrapper(q.getId(),q.getQuestionTitle(),q.getOption1(),q.getOption2(),q.getOption3(),q.getOption4())).toList();
        return new ResponseEntity<>(wrappers,HttpStatus.OK);
    }

    public ResponseEntity<?> getScore(List<Response> responses)
    {
        int right=0;
        for(Response response : responses)
        {
            Question question=questionRepository.findById(response.getId()).get();
            if(response.getResponse().equals(question.getRightAnswer()))
                right++;
        }

        return new ResponseEntity<>(right,HttpStatus.OK);
    }
}
