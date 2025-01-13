package com.crescenda.backend.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crescenda.backend.response.PurchaseDetailsResponse;
import com.crescenda.backend.service.MentorDashboardService;
import com.crescenda.backend.service.serviceImpl.MentorDashboardServiceImpl.DailyEarningsResponse;


@RestController
@RequestMapping("/api/mentor/mentor-dashboard")
public class MentorDashboardController {

    private final MentorDashboardService mentorDashboardService;

    public MentorDashboardController(MentorDashboardService mentorDashboardService) {
        this.mentorDashboardService = mentorDashboardService;
    }

    @GetMapping("/{mentorId}")
    public MentorDashboardResponse getDashboardData(@PathVariable Long mentorId) {
        double totalEarnings = mentorDashboardService.getTotalEarnings(mentorId);
        double monthlyEarnings = mentorDashboardService.getMonthlyEarnings(mentorId);
        long totalPurchases = mentorDashboardService.getTotalPurchases(mentorId);
        long activeCourses = mentorDashboardService.getActiveCourses(mentorId);

        return new MentorDashboardResponse(totalEarnings, monthlyEarnings, totalPurchases, activeCourses);
    }
    
    @GetMapping("/{mentorId}/earnings")
    public List<DailyEarningsResponse> getDailyEarnings(
            @PathVariable Long mentorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate
    ) {
        return mentorDashboardService.getDailyEarnings(mentorId, fromDate, toDate);
    }


    @GetMapping("/{mentorId}/purchases")
    public List<PurchaseDetailsResponse> getPurchaseDetailsByMentorId(@PathVariable Long mentorId) {
        return mentorDashboardService.getPurchaseDetailsByMentorId(mentorId);
    }
    
    record MentorDashboardResponse(double totalEarnings, double monthlyEarnings, long totalPurchases, long activeCourses) {}
}