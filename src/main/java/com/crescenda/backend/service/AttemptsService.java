package com.crescenda.backend.service;

import java.util.Map;

public interface AttemptsService {

	Map<String, Object> getAttemptDetails(Long courseId, Long studentId);

}
