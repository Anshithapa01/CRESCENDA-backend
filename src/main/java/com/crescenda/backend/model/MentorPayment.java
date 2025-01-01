package com.crescenda.backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class MentorPayment {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "commission_deducted")
    private BigDecimal commissionDeducted;

    @Column(name = "payment_due_date")
    private LocalDate paymentDueDate;

    @Column(name = "payment_status")
    private String paymentStatus; //paid,due

    @Column(name = "last_payment_date")
    private LocalDate lastPaymentDate;

	public MentorPayment(Long paymentId, BigDecimal totalAmount, BigDecimal commissionDeducted,
			LocalDate paymentDueDate, String paymentStatus, LocalDate setLastPaymentDate) {
		super();
		this.paymentId = paymentId;
		this.totalAmount = totalAmount;
		this.commissionDeducted = commissionDeducted;
		this.paymentDueDate = paymentDueDate;
		this.paymentStatus = paymentStatus;
		this.lastPaymentDate = setLastPaymentDate;
	}

	//Relationships 
	
	@ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;
}
