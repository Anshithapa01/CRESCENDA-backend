package com.crescenda.backend.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crescenda.backend.model.Course;
import com.crescenda.backend.model.Enrollment;
import com.crescenda.backend.model.MentorPayment;
import com.crescenda.backend.repository.EnrollmentRepository;
import com.crescenda.backend.repository.MentorPaymentRepository;
import com.crescenda.backend.repository.RatingRepository;
import com.crescenda.backend.response.PurchaseDetailsResponse;
import com.crescenda.backend.service.AdminDashboardService;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService{
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	@Autowired
	MentorPaymentRepository mentorPaymentRepository;
	@Autowired
	RatingRepository ratingRepository;
	
	
	@Override
	public Map<String, Object> calculateLifetimeMetrics() {
	    // Lifetime metrics from enrollment and mentor payments
	    BigDecimal lifetimeTotalEnrollmentAmount = enrollmentRepository.findTotalEnrollmentAmount();

	    List<MentorPayment> mentorPayments = mentorPaymentRepository.findAll();

	    BigDecimal lifetimeEarnings = mentorPayments.stream()
	        .map(MentorPayment::getTotalAmount)
	        .reduce(BigDecimal.ZERO, BigDecimal::add);

	    BigDecimal lifetimeCommission = mentorPayments.stream()
	        .map(MentorPayment::getCommissionDeducted)
	        .reduce(BigDecimal.ZERO, BigDecimal::add);
	    
	    List<Object[]> weeklyData = enrollmentRepository.findWeeklyAmounts();
	    List<Object[]> monthlyData = enrollmentRepository.findMonthlyAmounts();
	    List<Object[]> yearlyData = enrollmentRepository.findYearlyAmounts();
	    
	    List<Map<String, Object>> formattedMonthlyData = monthlyData.stream()
	    	    .map(record -> Map.of(
	    	        "month", record[0], // Extracted month number
	    	        "amount", record[2] // Total amount for the month
	    	    ))
	    	    .collect(Collectors.toList());

	    // Process results into structured data
	    

	    double todaysAmount = enrollmentRepository.findTodaysAmount();
	    double thisWeeksAmount = enrollmentRepository.findThisWeeksAmount();
	    double thisMonthsAmount = enrollmentRepository.findThisMonthsAmount();
	    double thisYearsAmount =enrollmentRepository.findThisYearsAmount();
	    double thisMonthsCommission = mentorPaymentRepository.findThisMonthsCommissionDeducted();

	    // Rating statistics
	    long totalReviews = ratingRepository.findTotalReviewCount();
	    long oneStarReviews = ratingRepository.countReviewsByRating(1);
	    long twoStarReviews = ratingRepository.countReviewsByRating(2);
	    long threeStarReviews = ratingRepository.countReviewsByRating(3);
	    long fourStarReviews = ratingRepository.countReviewsByRating(4);
	    long fiveStarReviews = ratingRepository.countReviewsByRating(5);

	    // Prepare the response map
	    Map<String, Object> response = new HashMap<>();
	    
	    response.put("weeklyData", processGroupedData(weeklyData, "day"));
	    response.put("monthlyData", formattedMonthlyData);
	    response.put("yearlyData", processGroupedData(yearlyData, "year"));
	    
	    response.put("lifetimeTotalEnrollmentAmount", lifetimeTotalEnrollmentAmount);
	    response.put("lifetimeEarnings", lifetimeEarnings);
	    response.put("lifetimeCommission", lifetimeCommission);
	    response.put("todaysAmount", todaysAmount);
	    response.put("thisWeeksAmount", thisWeeksAmount);
	    response.put("thisMonthsAmount", thisMonthsAmount);
	    response.put("thisYearsAmount", thisYearsAmount);
	    response.put("thisMonthsCommission", thisMonthsCommission);

	    // Add review statistics
	    response.put("totalReviews", totalReviews);
	    response.put("1StarReviews", oneStarReviews);
	    response.put("2StarReviews", twoStarReviews);
	    response.put("3StarReviews", threeStarReviews);
	    response.put("4StarReviews", fourStarReviews);
	    response.put("5StarReviews", fiveStarReviews);

	    return response;
	}
	
	private List<Map<String, Object>> processGroupedData(List<Object[]> groupedData, String label) {
	    List<Map<String, Object>> dataList = new ArrayList<>();
	    for (Object[] row : groupedData) {
	        Map<String, Object> data = new HashMap<>();
	        data.put(label, row[0]); // e.g., day, month, or year
	        data.put("amount", row[1]);
	        dataList.add(data);
	    }
	    return dataList;
	}

	@Override
	public List<Map<String, Object>> getTopSellingCourses() {
	    Pageable topTen = PageRequest.of(0, 10); // Fetch top 10
	    List<Object[]> results = enrollmentRepository.findTopSellingCourses(topTen);

	    // Convert results to a readable format
	    List<Map<String, Object>> topCourses = new ArrayList<>();
	    for (Object[] result : results) {
	        Course course = (Course) result[0];
	        long enrollCount = (long) result[1];

	        Map<String, Object> courseData = new HashMap<>();
	        courseData.put("thumbnailUrl", 
	            course.getDraft() != null ? course.getDraft().getThumbnailUrl() : null);
	        courseData.put("courseName", 
	            course.getDraft() != null ? course.getDraft().getCourseName() : null);
	        courseData.put("courseDescription", 
	            course.getDraft() != null ? course.getDraft().getCourseDescription() : null); 
	        courseData.put("type", 
	            course.getDraft() != null ? course.getDraft().getType() : null); 
	        courseData.put("salePrice", 
	            course.getDraft() != null ? course.getDraft().getSellingPrice() : null);
	        courseData.put("enrollmentCount", enrollCount);

	        topCourses.add(courseData);
	    }
	    return topCourses;
	}

	@Override
	public List<PurchaseDetailsResponse> getAllPurchases() {
        // Fetch all enrollments
        List<Enrollment> enrollments = enrollmentRepository.findAll();

        // Map enrollments to response class
        return enrollments.stream()
                .map(enrollment -> new PurchaseDetailsResponse(
                		enrollment.getPaymentId(),
                        enrollment.getCourse().getDraft().getThumbnailUrl(),
                        enrollment.getCourse().getDraft().getCourseName(),
                        enrollment.getStudent().getFirstName(),
                        enrollment.getEnrollmentDate(),
                        enrollment.getAmount()))
                .collect(Collectors.toList());
    }
	
	@Override
	public List<Map<String, Object>> getDailyTotals(LocalDateTime fromDate, LocalDateTime toDate) {
	    List<Object[]> dailyData = enrollmentRepository.findDailyTotalsByDateRange(fromDate, toDate);

	    // Map results into a readable format
	    return dailyData.stream()
	        .map(record -> Map.of(
	            "day", record[0], // Date as a string
	            "totalAmount", record[1] // Total amount for the day
	        ))
	        .collect(Collectors.toList());
	}

}
