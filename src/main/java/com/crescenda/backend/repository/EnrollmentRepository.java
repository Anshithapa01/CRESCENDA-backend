package com.crescenda.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crescenda.backend.model.Enrollment;
import com.crescenda.backend.model.Payment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Enrollment findByPayment(Payment payment);
}