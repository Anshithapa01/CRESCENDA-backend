package com.crescenda.backend.repository;
import com.crescenda.backend.model.Enrollment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    List<Enrollment> findByStudentStudentId(int studentId);
    Enrollment findByPaymentId(String paymentId);
    boolean existsByCourseCourseIdAndStudentStudentId(int courseId, int studentId);
    
    @Query("SELECT COUNT(e) FROM Enrollment e JOIN e.course c JOIN c.draft d WHERE d.mentor.mentorId = :mentorId")
    Long getMentorTotalPurchases(@Param("mentorId") Long mentorId);
    
    
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Enrollment e")
    BigDecimal findTotalEnrollmentAmount();
    
    
    @Query("""
    	    SELECT COALESCE(SUM(e.amount), 0) 
    	    FROM Enrollment e 
    	    WHERE CAST(e.enrollmentDate AS date) = CURRENT_DATE
    	""")
    double findTodaysAmount();

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Enrollment e " +
    	       "WHERE EXTRACT(YEAR FROM e.enrollmentDate) = EXTRACT(YEAR FROM CURRENT_DATE) " +
    	       "AND EXTRACT(WEEK FROM e.enrollmentDate) = EXTRACT(WEEK FROM CURRENT_DATE)")
    double findThisWeeksAmount();

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0) 
        FROM Enrollment e 
        WHERE MONTH(e.enrollmentDate) = MONTH(CURRENT_DATE) AND YEAR(e.enrollmentDate) = YEAR(CURRENT_DATE)
    """)
    double findThisMonthsAmount();
    
    @Query("""
    	    SELECT COALESCE(SUM(e.amount), 0) 
    	    FROM Enrollment e 
    	    WHERE EXTRACT(YEAR FROM e.enrollmentDate) = EXTRACT(YEAR FROM CURRENT_DATE)
    	""")
    	double findThisYearsAmount();

    @Query("""
            SELECT e.course, COUNT(e.course) AS enrollCount 
            FROM Enrollment e 
            GROUP BY e.course 
            ORDER BY enrollCount DESC
        """)
        List<Object[]> findTopSellingCourses(Pageable pageable);
        
    @Query("""
    	    SELECT TO_CHAR(e.enrollmentDate, 'Day') AS day, COALESCE(SUM(e.amount), 0) AS total
    	    FROM Enrollment e
    	    WHERE EXTRACT(WEEK FROM e.enrollmentDate) = EXTRACT(WEEK FROM CURRENT_DATE)
    	      AND EXTRACT(YEAR FROM e.enrollmentDate) = EXTRACT(YEAR FROM CURRENT_DATE)
    	    GROUP BY TO_CHAR(e.enrollmentDate, 'Day')
    	    ORDER BY MIN(e.enrollmentDate)
    	""")
    	List<Object[]> findWeeklyAmounts();

	@Query("""
		    SELECT EXTRACT(MONTH FROM e.enrollmentDate) AS month, 
		           TO_CHAR(e.enrollmentDate, 'Month') AS monthName, 
		           COALESCE(SUM(e.amount), 0) AS total
		    FROM Enrollment e
		    WHERE EXTRACT(YEAR FROM e.enrollmentDate) = EXTRACT(YEAR FROM CURRENT_DATE)
		    GROUP BY EXTRACT(MONTH FROM e.enrollmentDate), TO_CHAR(e.enrollmentDate, 'Month')
		    ORDER BY month
		""")
		List<Object[]> findMonthlyAmounts();

	@Query("""
		    SELECT EXTRACT(YEAR FROM e.enrollmentDate) AS year, COALESCE(SUM(e.amount), 0) AS total
		    FROM Enrollment e
		    GROUP BY EXTRACT(YEAR FROM e.enrollmentDate)
		    ORDER BY year
		""")
		List<Object[]> findYearlyAmounts();
		
		List<Enrollment> findAll();

		
		@Query("""
			    SELECT 
			        TO_CHAR(e.enrollmentDate, 'YYYY-MM-DD') AS day, 
			        COALESCE(SUM(e.amount * 0.55), 0) AS totalEarnings 
			    FROM Enrollment e 
			    WHERE e.course.draft.mentor.mentorId = :mentorId 
			      AND e.enrollmentDate BETWEEN :fromDate AND :toDate 
			    GROUP BY TO_CHAR(e.enrollmentDate, 'YYYY-MM-DD') 
			    ORDER BY day
			""")
			List<Object[]> findDailyEarningsByMentorId(
			    @Param("mentorId") Long mentorId, 
			    @Param("fromDate") LocalDateTime fromDate, 
			    @Param("toDate") LocalDateTime toDate
			);

			@Query("""
				    SELECT 
				        TO_CHAR(e.enrollmentDate, 'YYYY-MM-DD') AS day, 
				        COALESCE(SUM(e.amount), 0) AS totalAmount 
				    FROM Enrollment e 
				    WHERE e.enrollmentDate BETWEEN :fromDate AND :toDate 
				    GROUP BY TO_CHAR(e.enrollmentDate, 'YYYY-MM-DD') 
				    ORDER BY day
				""")
				List<Object[]> findDailyTotalsByDateRange(
				    @Param("fromDate") LocalDateTime fromDate, 
				    @Param("toDate") LocalDateTime toDate
				);
}
