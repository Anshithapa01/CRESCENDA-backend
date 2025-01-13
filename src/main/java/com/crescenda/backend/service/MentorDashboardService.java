package com.crescenda.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.crescenda.backend.response.PurchaseDetailsResponse;
import com.crescenda.backend.service.serviceImpl.MentorDashboardServiceImpl.DailyEarningsResponse;

public interface MentorDashboardService {

	long getActiveCourses(Long mentorId);

	long getTotalPurchases(Long mentorId);

	double getMonthlyEarnings(Long mentorId);

	double getTotalEarnings(Long mentorId);

	List<PurchaseDetailsResponse> getPurchaseDetailsByMentorId(Long mentorId);

	List<DailyEarningsResponse> getDailyEarnings(Long mentorId, LocalDateTime fromDate, LocalDateTime toDate);

}
