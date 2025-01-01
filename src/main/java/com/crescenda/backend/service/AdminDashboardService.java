package com.crescenda.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.crescenda.backend.response.PurchaseDetailsResponse;

public interface AdminDashboardService {

	Map<String, Object> calculateLifetimeMetrics();

	List<Map<String, Object>> getTopSellingCourses();

	List<PurchaseDetailsResponse> getAllPurchases();

	List<Map<String, Object>> getDailyTotals(LocalDateTime fromDate, LocalDateTime toDate);

}
