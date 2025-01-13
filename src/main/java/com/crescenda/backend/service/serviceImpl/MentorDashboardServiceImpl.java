package com.crescenda.backend.service.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.crescenda.backend.model.MentorPayment;
import com.crescenda.backend.model.Payment;
import com.crescenda.backend.repository.CourseRepository;
import com.crescenda.backend.repository.MentorPaymentRepository;
import com.crescenda.backend.repository.PaymentRepository;
import com.crescenda.backend.response.PurchaseDetailsResponse;
import com.crescenda.backend.service.MentorDashboardService;

@Service
public class MentorDashboardServiceImpl implements MentorDashboardService{
	
	private final PaymentRepository paymentRepository;
    private final CourseRepository courseRepository;
    private final MentorPaymentRepository mentorPaymentRepository;

    public MentorDashboardServiceImpl(PaymentRepository paymentRepository,
    		CourseRepository courseRepository,
    		MentorPaymentRepository mentorPaymentRepository) {
        this.paymentRepository = paymentRepository;
        this.courseRepository = courseRepository;
        this.mentorPaymentRepository=mentorPaymentRepository;
    }

    @Override
    public double getTotalEarnings(Long mentorId) {
        // Fetch all payments for the mentor and sum their totalAmount values
        BigDecimal totalEarnings = mentorPaymentRepository.findTotalEarningsByMentorId(mentorId);
        return totalEarnings != null ? totalEarnings.doubleValue() : 0;
    }

    @Override
    public double getMonthlyEarnings(Long mentorId) {
        // Fetch the last record with status "due" and get its totalAmount value
        MentorPayment latestDuePayment = mentorPaymentRepository.findLatestDuePaymentByMentorId(mentorId);
        return latestDuePayment != null ? latestDuePayment.getTotalAmount().doubleValue() : 0;
    }

    @Override
    public long getTotalPurchases(Long mentorId) {
        return paymentRepository.getMentorTotalPurchases(mentorId);
    }

    @Override
    public long getActiveCourses(Long mentorId) {
        return courseRepository.getActiveCoursesForMentor(mentorId);
    }

    @Override
    public List<PurchaseDetailsResponse> getPurchaseDetailsByMentorId(Long mentorId) {
        List<Payment> enrollments = paymentRepository.findAll();

        // Filter enrollments where the course's draft is associated with the mentor
        return enrollments.stream()
                .filter(enrollment -> enrollment.getCourse().getDraft().getMentor().getMentorId().equals(mentorId))
                .map(enrollment -> new PurchaseDetailsResponse(
                		enrollment.getPaymentId(),
                        enrollment.getCourse().getDraft().getThumbnailUrl(),
                        enrollment.getCourse().getDraft().getCourseName(),
                        enrollment.getStudent().getFirstName(),
                        enrollment.getPaymentDate(),
                        enrollment.getAmount()
                ))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<DailyEarningsResponse> getDailyEarnings(Long mentorId, LocalDateTime fromDate, LocalDateTime toDate) {
        List<Object[]> results = paymentRepository.findDailyEarningsByMentorId(mentorId, fromDate, toDate);

        // Map results into a response format
        return results.stream()
            .map(result -> new DailyEarningsResponse(
                LocalDate.parse((String) result[0]), // Parse day
                ((Double) result[1]) // Total earnings
            ))
            .collect(Collectors.toList());
    }

    public record DailyEarningsResponse(LocalDate day, double totalEarnings) {}
}
