package com.crescenda.backend.service;

import java.util.List;

import com.crescenda.backend.model.Quiz;
import com.crescenda.backend.request.QuizRequest;
import com.crescenda.backend.request.QuizSubmissionRequest;
import com.crescenda.backend.response.QuizResponse;
import com.crescenda.backend.response.QuizResultResponse;

public interface QuizService {

	void deleteQuiz(Long quizId);

	Quiz addQuiz(QuizRequest quiz);

	List<QuizResponse> getQuizzesByDraftId(Long draftId);

	Quiz updateQuiz(Long quizId, QuizRequest updatedQuizRequest);

	QuizResultResponse evaluateQuiz(QuizSubmissionRequest request);

}
