package com.crescenda.backend.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crescenda.backend.model.Mentor;
import com.crescenda.backend.model.MentorPayment;

public interface MentorPaymentRepository extends JpaRepository<MentorPayment, Long> {
	Optional<MentorPayment> findByMentor(Mentor mentor);
	
	//mentor 
	
	@Query("SELECT COALESCE(SUM(mp.totalAmount), 0) FROM MentorPayment mp WHERE mp.mentor.id = :mentorId")
    BigDecimal findTotalEarningsByMentorId(@Param("mentorId") Long mentorId);
	
    @Query("SELECT mp FROM MentorPayment mp WHERE mp.mentor.id = :mentorId AND mp.paymentStatus = 'due' ORDER BY mp.paymentDueDate DESC")
    MentorPayment findLatestDuePaymentByMentorId(@Param("mentorId") Long mentorId);
    
    List<MentorPayment> findByPaymentStatus(String paymentStatus);
    
    Optional<MentorPayment> findTopByMentorAndPaymentStatusOrderByPaymentDueDateDesc(Mentor mentor, String paymentStatus);
    
    List<MentorPayment> findByMentor_MentorId(Long mentorId);
    

    @Query(value = """
    	    SELECT COALESCE(SUM(mp.commission_deducted), 0) 
    	    FROM mentor_payment mp 
    	    WHERE EXTRACT(YEAR FROM mp.last_payment_date) = EXTRACT(YEAR FROM CURRENT_DATE)
		      AND EXTRACT(MONTH FROM mp.last_payment_date) = EXTRACT(MONTH FROM CURRENT_DATE)
		""", nativeQuery = true)
    double findThisMonthsCommissionDeducted();
    
}