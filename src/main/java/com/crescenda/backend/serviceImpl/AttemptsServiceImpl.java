package com.crescenda.backend.serviceImpl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.crescenda.backend.model.Attempt;
import com.crescenda.backend.repository.AttemptRepository;
import com.crescenda.backend.service.AttemptsService;

@Service
public class AttemptsServiceImpl implements AttemptsService{
	
	private final AttemptRepository attemptRepository;

    public AttemptsServiceImpl(AttemptRepository attemptRepository) {
        this.attemptRepository = attemptRepository;
    }

    @Override
    public Map<String, Object> getAttemptDetails(Long courseId, Long studentId) {
        List<Attempt> attempts = attemptRepository.findByDraft_DraftIdAndStudent_StudentId(courseId, studentId);

        // Check for "pass" status and extract pass date
        Optional<Attempt> passedAttempt = attempts.stream()
                .filter(attempt -> "pass".equalsIgnoreCase(attempt.getStatus()))
                .findFirst();
        LocalDate passDate = passedAttempt.map(Attempt::getAttemptDate).orElse(null);

        // Build the response map
        Map<String, Object> response = new HashMap<>();
        response.put("attemptCount", attempts.size());
        response.put("hasPassed", passedAttempt.isPresent());
        response.put("passDate", passDate);

        return response;
    }

}
