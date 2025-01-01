package com.crescenda.backend.controller;

import com.crescenda.backend.model.Attempt;
import com.crescenda.backend.model.Course;
import com.crescenda.backend.model.Draft;
import com.crescenda.backend.model.Quiz;
import com.crescenda.backend.repository.AttemptRepository;
import com.crescenda.backend.repository.CourseRepository;
import com.crescenda.backend.repository.DraftRepository;
import com.crescenda.backend.repository.QuizRepository;
import com.crescenda.backend.request.QuizRequest;
import com.crescenda.backend.request.QuizSubmissionRequest;
import com.crescenda.backend.response.CourseResponse;
import com.crescenda.backend.response.QuizResponse;
import com.crescenda.backend.response.QuizResultResponse;
import com.crescenda.backend.service.AttemptsService;
import com.crescenda.backend.service.CourseService;
import com.crescenda.backend.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/qa/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;
	@Autowired
    private AttemptsService attemptsService;
	
    @GetMapping("/draft/{draftId}")
    public ResponseEntity<List<QuizResponse>> getQuizzesByDraftId(@PathVariable Long draftId) {
        List<QuizResponse> quizzes = quizService.getQuizzesByDraftId(draftId);
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }


    @PostMapping
    public Quiz addQuiz(@RequestBody QuizRequest quizRequest) {
    	System.out.println(quizRequest);
        return quizService.addQuiz(quizRequest);
    }

    @PutMapping("/{quizId}")
    public Quiz updateQuiz(@PathVariable Long quizId, @RequestBody QuizRequest quizRequest) {
        return quizService.updateQuiz(quizId, quizRequest);
    }


    @DeleteMapping("/{quizId}")
    public void deleteQuiz(@PathVariable Long quizId) {
        quizService.deleteQuiz(quizId);
    }
    
    @PostMapping("/submit")
    public ResponseEntity<QuizResultResponse> submitQuiz(@RequestBody QuizSubmissionRequest request) {
        QuizResultResponse result = quizService.evaluateQuiz(request);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/attempts/{courseId}/{studentId}")
    public ResponseEntity<Map<String, Object>> getAttempts(@PathVariable Long courseId, @PathVariable Long studentId) {
        Map<String, Object> response = attemptsService.getAttemptDetails(courseId, studentId);
        return ResponseEntity.ok(response);
    }


}
