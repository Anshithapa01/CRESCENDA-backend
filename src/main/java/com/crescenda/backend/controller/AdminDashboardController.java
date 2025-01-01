package com.crescenda.backend.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crescenda.backend.response.PurchaseDetailsResponse;
import com.crescenda.backend.service.AdminDashboardService;

@RestController
@RequestMapping("/api/admin")
public class AdminDashboardController {
	
	
	@Autowired
    private AdminDashboardService adminDashboardService;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getLifetimeMetrics() {
        Map<String, Object> result = adminDashboardService.calculateLifetimeMetrics();
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/top-selling")
    public ResponseEntity<List<Map<String, Object>>> getTopSellingCourses() {
        List<Map<String, Object>> topSellingCourses = adminDashboardService.getTopSellingCourses();
        System.out.println(topSellingCourses);
        return ResponseEntity.ok(topSellingCourses);
    }
    
    @GetMapping("/all-purchases")
    public ResponseEntity<List<PurchaseDetailsResponse>> getAllPurchases() {
        List<PurchaseDetailsResponse> allPurchases = adminDashboardService.getAllPurchases();
        return ResponseEntity.ok(allPurchases);
    }
    
    @GetMapping("/daily-totals")
    public ResponseEntity<List<Map<String, Object>>> getDailyTotals(
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
        List<Map<String, Object>> dailyTotals = adminDashboardService.getDailyTotals(fromDate, toDate);
        return ResponseEntity.ok(dailyTotals);
    }

}
