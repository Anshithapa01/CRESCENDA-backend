package com.crescenda.backend.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.crescenda.backend.model.MentorPayment;
import com.crescenda.backend.repository.MentorPaymentRepository;
import com.crescenda.backend.service.MentorPaymentService;

import jakarta.transaction.Transactional;

@Service
public class MentorPaymentServiceImpl implements MentorPaymentService{
	
	@Autowired
    private MentorPaymentRepository mentorPaymentRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 1 * *") 
    public void processMonthlyPayments() {
        List<MentorPayment> pendingPayments = mentorPaymentRepository.findByPaymentStatus("due");

        for (MentorPayment payment : pendingPayments) {
            if (payment.getPaymentDueDate().isBefore(LocalDate.now()) || payment.getPaymentDueDate().isEqual(LocalDate.now())) {
                payment.setPaymentStatus("paid");
                payment.setLastPaymentDate(LocalDate.now());
                mentorPaymentRepository.save(payment);
                
                MentorPayment newPayment = new MentorPayment();
                newPayment.setTotalAmount(BigDecimal.ZERO); 
                newPayment.setCommissionDeducted(BigDecimal.ZERO); 
                newPayment.setLastPaymentDate(LocalDate.now()); 
                newPayment.setPaymentStatus("due");
                newPayment.setPaymentDueDate(getNextMonthFirstDate()); 
                newPayment.setMentor(payment.getMentor()); 
                
                mentorPaymentRepository.save(newPayment);
                
                System.out.println("Processed payment for mentor: " + payment.getMentor().getMentorId() + 
                                   ", Amount: " + payment.getTotalAmount());
                
                System.out.println("Created new payment record for mentor: " + payment.getMentor().getMentorId() + 
                        ", Due Date: " + newPayment.getPaymentDueDate());
                
            }
        }
    }
    
    private LocalDate getNextMonthFirstDate() {
        return LocalDate.now().plusMonths(1).withDayOfMonth(1);
    }

}
