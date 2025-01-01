package com.crescenda.backend.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crescenda.backend.model.Attempt;
import com.crescenda.backend.model.Course;
import com.crescenda.backend.model.Draft;
import com.crescenda.backend.model.Quiz;
import com.crescenda.backend.model.Student;
import com.crescenda.backend.repository.AttemptRepository;
import com.crescenda.backend.repository.DraftRepository;
import com.crescenda.backend.repository.QuizRepository;
import com.crescenda.backend.request.QuizRequest;
import com.crescenda.backend.request.QuizSubmissionRequest;
import com.crescenda.backend.response.CourseResponse;
import com.crescenda.backend.response.QuizResponse;
import com.crescenda.backend.response.QuizResultResponse;
import com.crescenda.backend.response.StudentResponse;
import com.crescenda.backend.service.QuizService;

@Service
public class QuizServiceImpl implements QuizService{

	@Autowired
    private QuizRepository quizRepository;
	@Autowired
	private DraftRepository draftRepository;	
	@Autowired
    private AttemptRepository attemptRepository;

	@Override
	public List<QuizResponse> getQuizzesByDraftId(Long draftId) {
	    List<Quiz> quizzes = quizRepository.findByDraft_DraftId(draftId);
	    
	    // Map Quiz entities to QuizResponse objects
	    return quizzes.stream().map(quiz -> {
	        QuizResponse response = new QuizResponse();
	        response.setQuizId(quiz.getQuizId());
	        response.setQuestion(quiz.getQuestion());
	        response.setAnswer(quiz.getAnswer());
	        response.setOption1(quiz.getOption1());
	        response.setOption2(quiz.getOption2());
	        response.setOption3(quiz.getOption3());
	        response.setOption4(quiz.getOption4());
	        return response;
	    }).collect(Collectors.toList());
	}


	@Override
    public Quiz addQuiz(QuizRequest quizRequest) {
		Draft draft = draftRepository.findById(quizRequest.getDraft())
                .orElseThrow(() -> new RuntimeException("Draft not found with ID: " + quizRequest.getDraft()));

        Quiz quiz = new Quiz();
        quiz.setQuestion(quizRequest.getQuestion());
        quiz.setOption1(quizRequest.getOption1());
        quiz.setOption2(quizRequest.getOption2());
        quiz.setOption3(quizRequest.getOption3());
        quiz.setOption4(quizRequest.getOption4());
        quiz.setAnswer(quizRequest.getAnswer());
        quiz.setDraft(draft);

        return quizRepository.save(quiz);
    }

	@Override
	public Quiz updateQuiz(Long quizId, QuizRequest updatedQuizRequest) {
	    // Find the existing quiz entity
	    Quiz existingQuiz = quizRepository.findById(quizId)
	        .orElseThrow(() -> new RuntimeException("Quiz not found with ID: " + quizId));
	    
	    // Update fields from the request
	    existingQuiz.setQuestion(updatedQuizRequest.getQuestion());
	    existingQuiz.setOption1(updatedQuizRequest.getOption1());
	    existingQuiz.setOption2(updatedQuizRequest.getOption2());
	    existingQuiz.setOption3(updatedQuizRequest.getOption3());
	    existingQuiz.setOption4(updatedQuizRequest.getOption4());
	    existingQuiz.setAnswer(updatedQuizRequest.getAnswer());
	    
	    // Save the updated quiz entity
	    return quizRepository.save(existingQuiz);
	}


	@Override
    public void deleteQuiz(Long quizId) {
        quizRepository.deleteById(quizId);
    }
	
	@Override
	public QuizResultResponse evaluateQuiz(QuizSubmissionRequest request) {
        // Fetch quizzes for the draft
        Draft draft = draftRepository.findById(request.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid draft ID"));

        List<Quiz> quizzes = quizRepository.findByDraft_DraftId(draft.getDraftId());

        int correctAnswers = 0;

        for (Quiz quiz : quizzes) {
            String submittedAnswer = request.getAnswers().get(quiz.getQuizId());
            if (submittedAnswer != null && submittedAnswer.equalsIgnoreCase(quiz.getAnswer())) {
                correctAnswers++;
            }
        }

        int totalQuestions = quizzes.size();
        String status = correctAnswers >= (totalQuestions * 0.5) ? "Pass" : "Fail";

        // Save attempt
        Attempt attempt = new Attempt();
        attempt.setAttemptDate(LocalDate.now());
        attempt.setScore(correctAnswers);
        attempt.setStatus(status);
        attempt.setTotalQuestion(totalQuestions);
        attempt.setDraft(draft);
        attempt.setStudent(new Student(request.getStudentId()));

        attempt = attemptRepository.save(attempt);

        return new QuizResultResponse(
                totalQuestions,
                correctAnswers,
                status,
                attempt.getAttemptDate(),
                attempt.getAttemptId()
        );
    }
}
