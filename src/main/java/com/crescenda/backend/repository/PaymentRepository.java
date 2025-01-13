package com.crescenda.backend.repository;
import com.crescenda.backend.model.Payment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByStudentStudentId(int studentId);
    Payment findByPaymentId(String paymentId);
    boolean existsByCourseCourseIdAndStudentStudentId(int courseId, int studentId);
    
    @Query("SELECT COUNT(p) FROM Payment p JOIN p.course c JOIN c.draft d WHERE d.mentor.mentorId = :mentorId AND p.paymentStatus = 'completed'")
    Long getMentorTotalPurchases(@Param("mentorId") Long mentorId);
    
    
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.paymentStatus = 'completed'")
    BigDecimal findTotalPaymentAmount();
    
    
    @Query("""
    	    SELECT COALESCE(SUM(p.amount), 0) 
    	    FROM Payment p 
    	    WHERE CAST(p.paymentDate AS date) = CURRENT_DATE AND p.paymentStatus = 'completed'
    	""")
    double findTodaysAmount();

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p " +
    	       "WHERE EXTRACT(YEAR FROM p.paymentDate) = EXTRACT(YEAR FROM CURRENT_DATE) " +
    	       "AND EXTRACT(WEEK FROM p.paymentDate) = EXTRACT(WEEK FROM CURRENT_DATE) " +
               "AND p.paymentStatus = 'completed'")
    double findThisWeeksAmount();

    @Query("""
        SELECT COALESCE(SUM(p.amount), 0) 
        FROM Payment p 
        WHERE MONTH(p.paymentDate) = MONTH(CURRENT_DATE) 
          AND YEAR(p.paymentDate) = YEAR(CURRENT_DATE)
          AND p.paymentStatus = 'completed'
    """)
    double findThisMonthsAmount();
    
    @Query("""
    	    SELECT COALESCE(SUM(p.amount), 0) 
    	    FROM Payment p 
    	    WHERE EXTRACT(YEAR FROM p.paymentDate) = EXTRACT(YEAR FROM CURRENT_DATE)
    	      AND p.paymentStatus = 'completed'
    	""")
    	double findThisYearsAmount();

    @Query("""
            SELECT p.course, COUNT(p.course) AS paymentCount 
            FROM Payment p 
            WHERE p.paymentStatus = 'completed'
            GROUP BY p.course 
            ORDER BY paymentCount DESC
        """)
        List<Object[]> findTopSellingCourses(Pageable pageable);
        
    @Query("""
    	    SELECT TO_CHAR(p.paymentDate, 'Day') AS day, COALESCE(SUM(p.amount), 0) AS total
    	    FROM Payment p
    	    WHERE EXTRACT(WEEK FROM p.paymentDate) = EXTRACT(WEEK FROM CURRENT_DATE)
    	      AND EXTRACT(YEAR FROM p.paymentDate) = EXTRACT(YEAR FROM CURRENT_DATE)
    	      AND p.paymentStatus = 'completed'
    	    GROUP BY TO_CHAR(p.paymentDate, 'Day')
    	    ORDER BY MIN(p.paymentDate)
    	""")
    	List<Object[]> findWeeklyAmounts();

	@Query("""
		    SELECT EXTRACT(MONTH FROM p.paymentDate) AS month, 
		           TO_CHAR(p.paymentDate, 'Month') AS monthName, 
		           COALESCE(SUM(p.amount), 0) AS total
		    FROM Payment p
		    WHERE EXTRACT(YEAR FROM p.paymentDate) = EXTRACT(YEAR FROM CURRENT_DATE)
		      AND p.paymentStatus = 'completed'
		    GROUP BY EXTRACT(MONTH FROM p.paymentDate), TO_CHAR(p.paymentDate, 'Month')
		    ORDER BY month
		""")
		List<Object[]> findMonthlyAmounts();

	@Query("""
		    SELECT EXTRACT(YEAR FROM p.paymentDate) AS year, COALESCE(SUM(p.amount), 0) AS total
		    FROM Payment p
		    WHERE p.paymentStatus = 'completed'
		    GROUP BY EXTRACT(YEAR FROM p.paymentDate)
		    ORDER BY year
		""")
		List<Object[]> findYearlyAmounts();
		
		List<Payment> findAll();

		
		@Query("""
			    SELECT 
			        TO_CHAR(p.paymentDate, 'YYYY-MM-DD') AS day, 
			        COALESCE(SUM(p.amount * 0.55), 0) AS totalEarnings 
			    FROM Payment p 
			    WHERE p.course.draft.mentor.mentorId = :mentorId 
			      AND p.paymentDate BETWEEN :fromDate AND :toDate 
			      AND p.paymentStatus = 'completed'
			    GROUP BY TO_CHAR(p.paymentDate, 'YYYY-MM-DD') 
			    ORDER BY day
			""")
			List<Object[]> findDailyEarningsByMentorId(
			    @Param("mentorId") Long mentorId, 
			    @Param("fromDate") LocalDateTime fromDate, 
			    @Param("toDate") LocalDateTime toDate
			);

			@Query("""
				    SELECT 
				        TO_CHAR(p.paymentDate, 'YYYY-MM-DD') AS day, 
				        COALESCE(SUM(p.amount), 0) AS totalAmount 
				    FROM Payment p 
				    WHERE p.paymentDate BETWEEN :fromDate AND :toDate 
				      AND p.paymentStatus = 'completed'
				    GROUP BY TO_CHAR(p.paymentDate, 'YYYY-MM-DD') 
				    ORDER BY day
				""")
				List<Object[]> findDailyTotalsByDateRange(
				    @Param("fromDate") LocalDateTime fromDate, 
				    @Param("toDate") LocalDateTime toDate
				);
}





